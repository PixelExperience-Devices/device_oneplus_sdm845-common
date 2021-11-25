#include <android/file_descriptor_jni.h>
#include <jni.h>

#define nullptr ((void*)0)

extern int jniGetFDFromFileDescriptor(JNIEnv* env, jobject fileDescriptor) {
    if (fileDescriptor == nullptr) {
      return -1;
    }
    return AFileDescriptor_getFd(env, fileDescriptor);
}
