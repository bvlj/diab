#!/usr/bin/env bash


readonly REMOTE_PATH="/sdcard/Documents/diab"
readonly TRAIN_FILES=("train_1.csv" "train_3.csv" "train_5.csv")
readonly TEST_FILES=("test_1.csv" "test_3.csv" "test_5.csv")

function has_bin() {
  which $1 &> /dev/null
  return $?
}


function setup_adb() {
  adb kill-server &> /dev/null
  adb start-server &> /dev/null
  return $?
}


function get_device() {
  adb devices | while read line; do
    if [[ ! "$line" == "" ]] && [[ $(echo ${line} | awk '{print $2}') = "device" ]]; then
      echo ${line} | awk '{print $1}'
      break
    fi
  done
}


function run_adb() {
  adb -s ${device} $@
  echo "$@" &> /dev/null
  return $?
}


##
# Main
#

if [[ $(has_bin adb) -ne 0 ]]; then
  echo "adb not found!"
  exit 1
fi

setup_adb
device=$(get_device) &> /dev/null

if [[ "${device}" == "" ]]; then
  echo "No device found!"
fi

output_path=$1

for f in ${TEST_FILES[@]}; do
  run_adb pull "${REMOTE_PATH}/${f}" "${output_path}/${f}"
done
