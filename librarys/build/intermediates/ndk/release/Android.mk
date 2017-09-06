LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := librarys
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\Android.mk \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\Application.mk \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\Android.mk \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\lapi.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\lauxlib.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\lbaselib.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\lcode.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\ldblib.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\ldebug.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\ldo.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\ldump.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\lfunc.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\lgc.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\linit.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\liolib.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\llex.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\lmathlib.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\lmem.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\loadlib.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\lobject.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\lopcodes.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\loslib.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\lparser.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\lstate.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\lstring.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\lstrlib.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\ltable.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\ltablib.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\ltm.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\lundump.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\lvm.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\lua\lzio.c \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\luajava\Android.mk \
	D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni\luajava\luajava.c \

LOCAL_C_INCLUDES += D:\Android\AndroidGitWork\bjmz\librarys\src\main\jni
LOCAL_C_INCLUDES += D:\Android\AndroidGitWork\bjmz\librarys\src\release\jni

include $(BUILD_SHARED_LIBRARY)
