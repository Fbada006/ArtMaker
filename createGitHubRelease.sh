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

# Retrieve the current GitHub Release Version from scripts/publish.gradle.kts...
current_release_version=$(grep 'libVersion =' scripts/publish.gradle.kts | sed 's/[^0-9.]//g')

echo "Current Release Version is: $current_release_version"

# Get the Release Type as User Input...
echo "Please select the type of Release:"
echo "1) Major (Incompatible API Changes)"
echo "2) Minor (Backward Compatible Functionality)"
echo "3) Patch (Backward Compatible BUg Fixes)"
read -rp "Please choose your Release (1/2/3): " release_type_choice

case $release_type_choice in
    1)
        release_type="Major"
        ;;
    2)
        release_type="Minor"
        ;;
    3)
        release_type="Patch"
        ;;
    *)
        echo "Invalid Choice"
        exit 1
        ;;
esac