/*
 * Copyright (C) 2019 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#define LOG_TAG "lineage.livedisplay@2.0-service.oneplus_sdm845"

#include <dlfcn.h>

#include <android-base/logging.h>
#include <binder/ProcessState.h>
#include <hidl/HidlTransportSupport.h>
#include "DisplayModes.h"
#include "PictureAdjustment.h"
#include "SunlightEnhancement.h"

#define SDM_DISP_LIB "libsdm-disp-apis.so"

using ::vendor::lineage::livedisplay::V2_0::IDisplayModes;
using ::vendor::lineage::livedisplay::V2_0::ISunlightEnhancement;
using ::vendor::lineage::livedisplay::V2_0::implementation::DisplayModes;
using ::vendor::lineage::livedisplay::V2_0::IPictureAdjustment;
using ::vendor::lineage::livedisplay::V2_0::implementation::PictureAdjustment;
using ::vendor::lineage::livedisplay::V2_0::implementation::SunlightEnhancement;

int main() {
    // Vendor backend
    void* libHandle = nullptr;
    int32_t (*disp_api_init)(uint64_t*, uint32_t) = nullptr;
    int32_t (*disp_api_deinit)(uint64_t, uint32_t) = nullptr;
    uint64_t cookie = 0;

    android::sp<IDisplayModes> dm;
    android::sp<PictureAdjustment> pa;
    android::sp<SunlightEnhancement> se;

    uint8_t services = 0;
    android::status_t status = android::OK;

    android::ProcessState::initWithDriver("/dev/binder");

    LOG(INFO) << "LiveDisplay HAL service is starting.";

    libHandle = dlopen(SDM_DISP_LIB, RTLD_NOW);
    if (libHandle == nullptr) {
        LOG(ERROR) << "Can not get " << SDM_DISP_LIB << " (" << dlerror() << ")";
        goto shutdown;
    }


    disp_api_init =
        reinterpret_cast<int32_t (*)(uint64_t*, uint32_t)>(dlsym(libHandle, "disp_api_init"));
    if (disp_api_init == nullptr) {
        LOG(ERROR) << "Can not get disp_api_init from " << SDM_DISP_LIB << " (" << dlerror()
                   << ")";
        goto shutdown;
    }

    disp_api_deinit =
        reinterpret_cast<int32_t (*)(uint64_t, uint32_t)>(dlsym(libHandle, "disp_api_deinit"));
    if (disp_api_deinit == nullptr) {
        LOG(ERROR) << "Can not get disp_api_deinit from " << SDM_DISP_LIB << " (" << dlerror()
                   << ")";
        goto shutdown;
    }

    status = disp_api_init(&cookie, 0);
    if (status != android::OK) {
        LOG(ERROR) << "Can not initialize " << SDM_DISP_LIB << " (" << status << ")";
        goto shutdown;
    }

    // DisplayModes
    dm = new DisplayModes();
    if (dm == nullptr) {
        LOG(ERROR)
            << "Can not create an instance of LiveDisplay HAL DisplayModes Iface, exiting.";
        goto shutdown;
    }
    if (DisplayModes::isSupported()) {
        services++;
    }

   // PictureAdjustment
    pa = new PictureAdjustment(libHandle, cookie);
    if (pa == nullptr) {
        LOG(ERROR)
            << "Can not create an instance of LiveDisplay HAL PictureAdjustment Iface, exiting.";
        goto shutdown;
    }
    if (pa->isSupported()) {
        services++;
    }

    // Sunlight Enhacnement
    se = new SunlightEnhancement();
    if (se == nullptr) {
        LOG(ERROR) << "Cannot register sunlight enhancement HAL service.";
        goto shutdown;
    }

    if (SunlightEnhancement::isSupported()) {
        services++;
    }

    // Shutdown if there are no services
    if (services == 0) {
        goto shutdown;
    }

    android::hardware::configureRpcThreadpool(services, true /*callerWillJoin*/);

    // DisplayModes service
    if (DisplayModes::isSupported()) {
        status = dm->registerAsService();
        if (status != android::OK) {
            LOG(ERROR) << "Could not register service for LiveDisplay HAL DisplayModes Iface ("
                        << status << ")";
            goto shutdown;
        }
    }

    // PictureAdjustment service
    if (pa->isSupported()) {
        status = pa->registerAsService();
        if (status != android::OK) {
            LOG(ERROR) << "Could not register service for LiveDisplay HAL PictureAdjustment Iface ("
                       << status << ")";
            goto shutdown;
        }
    }

    // Sunlight Enhacnement service
    if (SunlightEnhancement::isSupported()) {
        status = se->registerAsService();
        if (status != android::OK) {
            LOG(ERROR) << "Could not register service for LiveDisplay HAL SunlightEhancement Iface ("
                        << status << ")";
            goto shutdown;
        }
    }

    LOG(INFO) << "LiveDisplay HAL service ready.";

    android::hardware::joinRpcThreadpool();

shutdown:
    // Cleanup what we started
    if (disp_api_deinit != nullptr) {
        disp_api_deinit(cookie, 0);
    }

    if (libHandle != nullptr) {
        dlclose(libHandle);
    }

    // In normal operation, we don't expect the thread pool to shutdown
    LOG(ERROR) << "LiveDisplay HAL service is shutting down.";
    return 1;
}
