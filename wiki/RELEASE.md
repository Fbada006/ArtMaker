# Versioning

This project uses semantic versioning where:

Given a version number `MAJOR.MINOR.PATCH`, increment the:

- `MAJOR` version when you make incompatible API changes
- `MINOR` version when you add functionality in a backward compatible manner
- `PATCH` version when you make backward compatible bug fixes

You can read more of this strategy [here](https://semver.org/)


# Creating a new release

The steps are straightforward when doing a release as outline below:

1. Create a release branch in the format of `release/MAJOR.MINOR.PATCH` from `main`. Example `release/1.2.5`. Make sure that you have pulled the latest changes from `main` and that all the necessary PRs have been merged ready for the release.
2. Run the release branch created above and make sure there are no errors. If there are any errors, create an issue, open a PR, and merge against the release branch <span style="color:red">NOT</span> `main`
3. Update the `libVersion` variable in `scripts/publish.gradle.kts` to match the release version as set in the branch name.
4. Create a Github release making sure that the tag of the release is from the branch name. For example, in the branch `release/1.2.5`, the tag will be `v1.2.5`. In the target drop down next to the tag, make sure you select the `release/1.2.5` branch, <span style="color:red">NOT</span> `main`
5. Give the release a title and draft the relevant release notes. Pick the release notes from the PRs that have been merged and scheduled for this release.
6. Ensure the `Set as the latest release` checkbox at the bottom of the page is selected.
7. Publish the release and wait for the `publish` pipeline to run and upload the artefacts to maven. In case of a failure here, start again from number 2 above and repeat.
8. Open a PR from the release branch to the main branch, get a review, ensure all checks have passed, and merge. 

# Triggering The GitHub Release Pipeline

The following are the steps required to successfully trigger the GitHub Release Pipeline:

1. Ensure that you are on the Release Branch (locally) as the Bash Script will fail to execute otherwise. Also, make sure that the Release Branch exists remotely on GitHub.
2. Grant the Execute permission using the following command: `chmod +x releaseArtMaker.sh`
3. Ensure you have your GitHub Token stored in the **local.properties** file and named **GITHUB_TOKEN**. Please use the [Classic Personal Access Tokens](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens#personal-access-tokens-classic) as the [Fine Grained Personal Access Tokens](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens#fine-grained-personal-access-tokens) need a bit of configuration.
4. Run the executable Bash Script using the following command: `./releaseArtMaker.sh`
5. Select your Release Type from the choices provided using the number that corresponds to the Release Type.
6. Write your Release Notes as you would (you can use the hyphens as was done in the previous Release Notes) and press ENTER once to move to the next line. Press ENTER twice to complete your input.
7. Confirm your Release Notes by inputting "y" as prompted and you should get a confirmation message on the same after your GitHub Release has been created.