#!/usr/bin/env bash

LOCAL_DIR=$(dirname $0)
PARENT_DIR=${LOCAL_DIR}/..
KTLINT_BIN=${PARENT_DIR}/ktlint
ML_DIR=${LOCAL_DIR}/../_ml

function install_pre_push() {
  cp "${LOCAL_DIR}/pre-push.sh" "${PARENT_DIR}/.git/hooks/pre-push"
  chmod +x "${PARENT_DIR}/.git/hooks/pre-push"
}

function install_ktlint() {
  echo "Downloading ktlint..."
  curl -o ${KTLINT_BIN} -sSLO https://github.com/pinterest/ktlint/releases/download/0.34.2/ktlint
  chmod a+x ${KTLINT_BIN}
  ./${KTLINT_BIN} --apply-to-idea-project --android -y
  ./${KTLINT_BIN} --install-git-pre-commit-hook
  sed -i "s:xargs ktlint:xargs ./ktlint:" ${PARENT_DIR}/.git/hooks/pre-commit
}

function disable_git_track_changes() {
  git update-index --skip-worktree $1
  git update-index --assume-unchanged $1
}

function assume_models_unchanged() {
  disable_git_track_changes ${ML_DIR}/data/test_*.csv
  disable_git_track_changes ${ML_DIR}/data/train_*.csv
}

install_pre_push && install_ktlint && assume_models_unchanged
