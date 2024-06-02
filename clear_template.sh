#!/bin/bash

# This file for bash will clean up the template to start with no annoying branding.
# When executing this, following steps will be executed:
# - Empty README.md
# - Change the Gradle Project name to the first argument
# - Remove this 'clear_template.sh' file (optional, see further below)

# Arguments
# ./clear_template.sh (project_name) (package_group) [--no-destroy]
# Note:
# The --no-destroy argument prevents the script from deleting it self at the end

project_name=$1
package_group=$2

if [ -z "$project_name" ]
then
  echo "> Please pass an Project Name as the first argument"
  exit 1
fi

if [ -z "$package_group" ]
then
  echo "> Please pass an package as the second argument"
  exit 1
fi

echo "> project_name=$project_name"
echo "> Clearing README.md"
echo "" > README.md

function change_gradle_project_name () {
  echo "> Replacing ktor-server-template with $project_name in settings.gradle.kts"
  perl -pi -e "s/ktor-server-template/$project_name/g" settings.gradle.kts
}

function change_gradle_package_group () {
  echo "> Replacing template.group with $package_group in build.gradle.kts"
  perl -pi -e "s/template.group/$package_group/g" build.gradle.kts
}

change_gradle_project_name
change_gradle_package_group

if [ "$3" == "--no-destroy" ]
then
  echo "> --no-destroy prevented destruction of clear_template.sh"
else
  echo "> Removing clear_template.sh"
  rm clear_template.sh
fi