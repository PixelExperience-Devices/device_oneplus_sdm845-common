#!/system/bin/sh

# Setup amd enable ZRAM
	swapoff /dev/block/zram0
	rm /dev/block/zram0
	echo 1 > /sys/block/zram0/reset
	echo lz4 > /sys/block/zram0/comp_algorithm
	echo 8 > /sys/block/zram0/max_comp_streams
	echo 1073741824 > /sys/block/zram0/disksize
	mkswap /dev/block/zram0
	swapon /dev/block/zram0

# Adjust Virtual Memory
	echo 60 > /proc/sys/vm/swappiness
	echo 10 > /proc/sys/vm/dirty_background_ratio
	echo 60 > /proc/sys/vm/vfs_cache_pressure
	echo 3000 > /proc/sys/vm/dirty_writeback_centisecs
