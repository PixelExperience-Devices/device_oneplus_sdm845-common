LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE:= check_dynamic_partitions
LOCAL_MODULE_TAGS := optional
LOCAL_MODULE_CLASS := EXECUTABLES
LOCAL_SRC_FILES := check_dynamic_partitions
LOCAL_PRODUCT_MODULE := true
include $(BUILD_PREBUILT)
