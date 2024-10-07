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
4. Ensure that you are on the release branch (locally) as `releaseArtMaker.sh` will fail to execute otherwise. Also, make sure that the release branch exists remotely on GitHub.
5. Grant the Execute permission using the following command: `chmod +x releaseArtMaker.sh`
6. Ensure you have your GitHub Token stored in the **local.properties** file and named **GITHUB_TOKEN**. Please use the [Classic Personal Access Tokens](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens#personal-access-tokens-classic) as the [Fine Grained Personal Access Tokens](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens#fine-grained-personal-access-tokens) need a bit of configuration.
7. Run the now executable `releaseArtMaker.sh` using the following command: `./releaseArtMaker.sh`
8. Select your release type from the choices provided using the number that corresponds to it.
9. Write your release notes as you would (you can use the hyphens as was done in the previous release notes) and press ENTER once to move to the next line. Press ENTER twice to complete your input.
10. Confirm your release notes by inputting "y" as prompted and you should get a confirmation message on the same after your release has been created.
11. Go to the [Releases](https://github.com/Fbada006/ArtMaker/releases), navigate into your release, and ensure the `Set as the latest release` checkbox at the bottom of the page is selected.
12. Publish the release and wait for the `publish` pipeline to run and upload the artefacts to maven. In case of a failure here, start again from number 2 above and repeat.
13. Open a PR from the release branch to the main branch, get a review, ensure all checks have passed, and merge.