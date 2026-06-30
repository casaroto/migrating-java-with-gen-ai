# Remove `was_public` from `sample-app-mod-mvc`

## Goal

Remove the unused `com.ibm.websphere.appserver:was_public` dependency from the MVC sample so it builds without a locally installed WebSphere API jar.

## Scope

- Remove `was_public` from the Maven and Gradle dependency declarations.
- Remove README instructions that require installing `was_public.jar` locally.
- Preserve the WAR packaging and standard Java EE API dependency.
- Preserve Liberty deployment support, including the `Containerfile`, Liberty `server.xml` files, IBM deployment descriptors, and documentation about running on Liberty.

## Source-code impact

The current Java source contains no `com.ibm.*` imports or references to classes supplied by `was_public`. Therefore, no Java source replacement or deletion is required. If verification reveals an indirect compile-time dependency missed by the textual scan, replace it with the applicable standard Java EE API rather than restoring `was_public`.

## Verification

1. Confirm that `was_public` and `com.ibm.websphere.appserver` no longer occur outside generated build output.
2. Run the Maven test/package lifecycle for `sample-app-mod-mvc`.
3. Run the Gradle tests or build when the checked-in wrapper is usable.
4. Confirm that Liberty configuration and IBM descriptors remain unchanged.

## Non-goals

- Removing WebSphere Liberty as a supported deployment target.
- Removing IBM deployment descriptors.
- Migrating from `javax.*` to `jakarta.*`.
- Refactoring unrelated application code or build dependencies.
