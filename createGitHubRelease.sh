#!/bin/bash

# Please ensure that you grant the Execute permission when running it for the first time using the following command:
# chmod +x ./createGitHubRelease.sh

# Get the next release version based on the input...
get_next_release_version() {

  local current_github_release_version=$1
  local github_release_type=$2

  IFS='.' read -r -a github_release_version_parts <<< "$current_github_release_version"

  case $github_release_type in
          "Major")
            github_release_version_parts[0]=$((github_release_version_parts[0] + 1))
            github_release_version_parts[1]=0
            github_release_version_parts[2]=0
            ;;
          "Minor")
            github_release_version_parts[1]=$((github_release_version_parts[1] + 1))
            github_release_version_parts[2]=0
            ;;
          "Patch")
            github_release_version_parts[2]=$((github_release_version_parts[2] + 1))
            ;;
          *)
            echo "Invalid release type"
            exit 1
            ;;
  esac

  echo "${github_release_version_parts[0]}.${github_release_version_parts[1]}.${github_release_version_parts[2]}"

}

# Retrieve the current GitHub Release Version from scripts/publish.gradle.kts...
current_github_release_version=$(grep 'libVersion =' scripts/publish.gradle.kts | sed 's/[^0-9.]//g')

echo "Current Release Version is: $current_github_release_version"

# Get the Release Type as User Input...
echo "Please select the type of Release:"
echo "1) Major (Incompatible API Changes)"
echo "2) Minor (Backward Compatible Functionality)"
echo "3) Patch (Backward Compatible Bug Fixes)"
read -rp "Please choose your Release (1/2/3): " github_release_type_choice

case $github_release_type_choice in
    1)
        github_release_type="Major"
        ;;
    2)
        github_release_type="Minor"
        ;;
    3)
        github_release_type="Patch"
        ;;
    *)
        echo "Invalid Choice"
        exit 1
        ;;
esac

# Calculate the next GitHub Release version...
next_github_release_version=$(get_next_release_version "$current_github_release_version" "$github_release_type")
echo "The next GitHub Release version will be: $next_github_release_version"