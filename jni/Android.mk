LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := KPISchedulerSynchronizer
LOCAL_SRC_FILES := KPISchedulerSynchronizer.cpp

include $(BUILD_SHARED_LIBRARY)
