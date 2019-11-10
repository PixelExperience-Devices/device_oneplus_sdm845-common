LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE        := qdcm_calib_data_samsung_s6e3fc2x01_cmd_mode_dsi_panel.xml
LOCAL_MODULE_TAGS   := optional
LOCAL_MODULE_CLASS  := ETC
LOCAL_SRC_FILES     := qdcm_calib_data_samsung_s6e3fc2x01_cmd_mode_dsi_panel.xml
LOCAL_MODULE_PATH   := $(TARGET_OUT_VENDOR_OVERLAY_ETC)
include $(BUILD_PREBUILT)

include $(CLEAR_VARS)
LOCAL_MODULE        := qdcm_calib_data_samsung_sofef00_m_cmd_mode_dsi_panel.xml
LOCAL_MODULE_TAGS   := optional
LOCAL_MODULE_CLASS  := ETC
LOCAL_SRC_FILES     := qdcm_calib_data_samsung_sofef00_m_cmd_mode_dsi_panel.xml
LOCAL_MODULE_PATH   := $(TARGET_OUT_VENDOR_OVERLAY_ETC)
include $(BUILD_PREBUILT)
