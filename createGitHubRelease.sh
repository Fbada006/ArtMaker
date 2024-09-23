#!/bin/bash

# Please ensure that you grant the Execute permission when running it for the first time using the following command:
# chmod +x ./createGitHubRelease.sh

# Get the next release version based on the input...
get_next_release_version() {

  local current_release_version=$1
  local release_type=$2

  IFS='.' read -r -a version_parts <<< "$current_release_version"

  case $release_type in
          "Major")
            version_parts[0]=$((version_parts[0] + 1))
            version_parts[1]=0
            version_parts[2]=0
            ;;
          "Minor")
            version_parts[1]=$((version_parts[1] + 1))
            version_parts[2]=0
            ;;
          "Patch")
            version_parts[2]=$((version_parts[2] + 1))
            ;;
          *)
            echo "Invalid release type"
            exit 1
            ;;
  esac

  echo "${version_parts[0]}.${version_parts[1]}.${version_parts[2]}"

}