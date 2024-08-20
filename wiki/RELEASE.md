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
3. Create a Github release making sure that the tag of the release is from the branch name. For example, in the branch `release/1.2.5`, the tag will be `v1.2.5`. In the target drop down next to the tag, make sure you select the `release/1.2.5` branch, <span style="color:red">NOT</span> `main`.
4. Give the release a title and draft the relevant release notes. Pick the release notes from the PRs that have been merged and scheduled for this release.
5. Ensure the `Set as the latest release` checkbox at the bottom of the page is selected.
6. Publish the release and wait for the `publish` pipeline to run and upload the artefacts to maven. In case of a failure here, start again from number 2 above and repeat.
7. Open a PR from the release branch to the main branch, get a review, ensure all checks have passed, and merge. 
