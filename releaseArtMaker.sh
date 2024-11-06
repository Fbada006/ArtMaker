#!/bin/bash

# Check if branch starts with "release"
BRANCH_NAME=$(git rev-parse --abbrev-ref HEAD)
if [[ ! $BRANCH_NAME =~ ^release ]]; then
    echo "Error: This is not a release branch. A release branch has to start with the word \"release\". Exiting."
    exit 1
fi

# Store branch name
RELEASE_BRANCH_NAME=$BRANCH_NAME

# Retrieve the GitHub Personal Access Token from local.properties...
# Please make sure you use a Personal Access GitHub Token. For more information, please visit:
# https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens
GITHUB_PERSONAL_ACCESS_TOKEN=$(grep "GITHUB_PERSONAL_ACCESS_TOKEN" local.properties | cut -d'=' -f2 | tr -d '[:space:]')

# Terminate the Bash Script's execution if the GitHub Token cannot be found...
if [[ -z "$GITHUB_PERSONAL_ACCESS_TOKEN" ]]; then
    echo "Error: GITHUB_PERSONAL_ACCESS_TOKEN NOT FOUND. Please set it in the local.properties file..."
    exit 1
fi

# Function to get the next Release Version based on the input
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
            echo "Invalid Release Type"
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

# Get the GitHub Release Notes...
echo "Enter the GitHub Release Notes (end the input by pressing ENTER twice):"
github_release_notes=""
while true; do
    # Prompt the user for each line with a ">" symbol...
    read -rp "> " line
    # If the user enters an empty line, break the loop...
    [[ -z "$line" ]] && break
    # Prepend each line with "- " and append it to the release notes with a newline...
    github_release_notes+="- $line"$'\n'
done

# Remove the trailing newline...
github_release_notes=$(echo "$github_release_notes" | sed 's/\n$//')

# Create the Changelog Link...
previous_github_release_version="v$current_github_release_version"
new_github_release_version="v$next_github_release_version"
changelog_link="**Full Changelog**: https://github.com/Fbada006/ArtMaker/compare/$previous_github_release_version...$new_github_release_version"

# Add the Release Notes with two line breaks before the Changelog...
full_github_release_notes=$'**New Feature(s)**\n'"$github_release_notes"$'\n\n'"$changelog_link"

# Get the confirmation before creating the GitHub Release...
echo -e "The following GitHub Release will be created:"
echo "GitHub Release Version: $next_github_release_version"
echo -e "GitHub Release Notes:\n$full_github_release_notes"
read -rp "Proceed? (y/n): " confirmation

# Terminate the Bash Script's execution if either "n" or an invalid response has been input...
if [[ $confirmation != "y" ]]; then
    echo "GitHub Release Creation has been aborted..."
    exit 0
fi

# Create the GitHub Release using the GitHub REST API with jq...
github_repository="Fbada006/ArtMaker"

# Construct the JSON payload using jq...
json_payload=$(jq -n \
    --arg tag_name "v$next_github_release_version" \
    --arg target_commitish "$RELEASE_BRANCH_NAME" \
    --arg name "🎉 Release $next_github_release_version" \
    --arg body "$full_github_release_notes" \
    --argjson draft true \
    --argjson prerelease false \
    '{
        tag_name: $tag_name,
        target_commitish: $target_commitish,
        name: $name,
        body: $body,
        draft: $draft,
        prerelease: $prerelease
    }')

# Make the API request...
github_api_response=$(curl -s -X POST "https://api.github.com/repos/$github_repository/releases" \
    -H "Authorization: token $GITHUB_PERSONAL_ACCESS_TOKEN" \
    -H "Content-Type: application/json" \
    -d "$json_payload")

# Check if the release was created successfully...
if echo "$github_api_response" | grep -q '"id"'; then
    echo "GitHub Release has been created successfully!"
else
    echo "Failed to create the GitHub Release..."
    echo "Response: $github_api_response"
    # Optionally, you can exit with a non-zero status...
    exit 1
fi