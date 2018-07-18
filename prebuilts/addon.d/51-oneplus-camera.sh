#!/sbin/sh
#
# /system/addon.d/oneplus-camera.sh
# During a HavocOs upgrade, this script backs up OnePlus camera,
# /system is formatted and reinstalled, then the file is restored.
#

. /tmp/backuptool.functions

list_files() {
cat <<EOF
priv-app/OnePlusCamera/OnePlusCamera.apk
priv-app/OnePlusGallery/OnePlusGallery.apk
priv-app/OnePlusCameraService/OnePlusCameraService.apk
lib64/libopcamera.so
lib64/libstblur_capture_api.so
lib64/libstblur_preview_api.so
lib64/libjnistblur_capture_api.so
lib64/libopbaselib.so
lib64/libjni_cvface_api.so
lib64/libjnistblur_preview_api.so
lib64/libcvface_api.so
lib64/libopcameralib.so
etc/privapp-permissions-oem.xml
addon.d/51-oneplus-camera.sh
addon.d/addond_tail
addon.d/addond_head
EOF
}

case "$1" in
  backup)
    list_files | while read FILE DUMMY; do
      backup_file $S/$FILE
    done
  ;;
  restore)
    list_files | while read FILE REPLACEMENT; do
      R=""
      [ -n "$REPLACEMENT" ] && R="$S/$REPLACEMENT"
      [ -f "$C/$S/$FILE" ] && restore_file $S/$FILE $R
    done
  ;;
  pre-backup)
    # Stub
  ;;
  post-backup)
    # Stub
  ;;
  pre-restore)
    # Stub
  ;;
  post-restore)
    if [ -d "/postinstall" ]; then
      P="/postinstall/system"
    else
      P="/system"
    fi

    for i in $(list_files); do
      chown root:root "$P/$i"
      chmod 644 "$P/$i"
      chmod 755 "$(dirname "$P/$i")"
    done
  ;;
esac

