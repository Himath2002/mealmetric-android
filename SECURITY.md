# Security Policy

## Supported version

Security fixes are applied to the latest release on `main`.

## Reporting a vulnerability

Please do not open a public issue for a suspected security vulnerability. Use GitHub’s private vulnerability reporting option in the repository’s **Security** tab. Include the affected version, reproduction steps, impact, and any suggested mitigation.

If private reporting is unavailable, contact the maintainer through the profile linked in the repository and avoid sharing sensitive details publicly.

## Credentials

This repository must never contain live Nutritionix credentials, cloud-service configuration, keystores, or signing passwords. If a credential is exposed, revoke it at the provider immediately; deleting it from the latest commit is not sufficient because Git history may retain it.
