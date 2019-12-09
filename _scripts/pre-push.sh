#!/usr/bin/env bash
#
# - Prevent force push to master
# - Prevent delete master
# - Allow push to master only if the test are passing
#

BRANCH=$(git rev-parse --abbrev-ref HEAD)
PUSH_COMMAND=$(ps -ocommand= -p $PPID)
PROTECTED_BRANCHES="^(master|release-*)"
FORCE_PUSH="force|delete|-f"

if [[ "$BRANCH" =~ $PROTECTED_BRANCHES ]]; then
  if [[ "$PUSH_COMMAND" =~ $FORCE_PUSH ]]; then
    echo "Prevented force-push to protected branch \"$BRANCH\" by pre-push hook"
    exit 1
  fi

  ./$(dirname $0)/../../gradlew cAT
  exit $?
fi

exit 0
