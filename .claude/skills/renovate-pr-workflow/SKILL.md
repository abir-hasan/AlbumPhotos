---
name: renovate-pr-workflow
description: Procedure for processing open Renovate dependency PRs in this repo one by one - preflight, PR discovery, merging main, the 3-attempt fix rule, push/comment/label rules, dry runs, and the per-PR result record. Preloaded by the renovate-pr-fixer agent.
user-invocable: false
---

# Renovate PR workflow

## Preflight (stop with a clear message if any step fails)

1. `gh auth status` must succeed.
2. `git status --porcelain` must be empty. Never stash, reset, or otherwise touch the user's
   uncommitted work. Tell them to commit or stash, then stop.
3. Record the starting branch: `git branch --show-current`.
4. `git fetch origin --prune`.
5. Record `main`'s head: `git rev-parse --short origin/main`.

## Finding the PRs

Renovate runs with the repository owner's token, so its PRs are authored by `abir-hasan`, **not**
a bot. Identify them by head branch prefix and label, never by author:

```bash
gh pr list --state open --label dependencies --limit 100 \
  --json number,title,headRefName,url \
  --jq '[.[] | select(.headRefName | startswith("renovate/"))] | sort_by(.number)'
```

- If the user named PR numbers, keep only those. Record any named number that isn't an open
  Renovate PR as skipped.
- If no PRs remain, the run is finished. Go to reporting (nothing changed, so no report file).

## Failure label (live runs only)

```bash
gh label create needs-manual-fix --color B60205 \
  --description "Renovate PR the fixer agent could not repair" 2>/dev/null || true
```

## Per-PR procedure (ascending PR number, strictly one at a time)

1. **Checkout and read the PR**
   ```bash
   git checkout -B <branch> origin/<branch>
   gh pr view <N> --json body --jq .body
   ```
   From the body, note each dependency that changed and its old → new version.

2. **Merge main**
   ```bash
   git merge --no-edit origin/main
   ```
   - On conflicts, keep the PR's version bumps and `main`'s structural changes (catalog entries
     added or removed, plugins, build config). Then `git add` and `git commit --no-edit`.
     Resolving conflicts counts as part of attempt 1.
   - Note whether the merge brought in new commits: `git rev-list --count origin/<branch>..HEAD`.

3. **Verify** using the android-dependency-fixes skill.

4. **Passes with no fix needed**
   - The merge was a no-op → push nothing. Outcome `already-passing`.
   - The merge added commits → `git push origin <branch>` and comment
     "Merged latest `main`; build, tests, detekt and ktlint pass." Outcome `merged-main-only`.

5. **Fails → up to 3 attempts.** Each attempt is one diagnose → change → re-verify cycle, using
   the android-dependency-fixes skill. Network retries don't count as attempts. Stop as soon as
   verification passes.

6. **Success after a fix**
   - Commit all fix changes as **one** commit. The merge commit stays separate:
     ```
     fix(deps): make <dependency> <new version> build

     - <fix 1>
     - <fix 2>

     Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>
     ```
   - `git push origin <branch>`. Use a normal push, never `--force`.
   - Write the comment to a temp file and post it with `gh pr comment <N> --body-file <file>`.
     Include: what broke, the root cause with sources, what changed in each file, and that
     build/tests/detekt/ktlint now pass.
   - Outcome `fixed`.

7. **Still failing after 3 attempts**
   - `git reset --hard origin/<branch>`, so nothing broken stays or gets pushed.
   - `gh pr edit <N> --add-label needs-manual-fix`.
   - Comment with the last error excerpt and what each of the 3 attempts tried.
   - Outcome `failed`.

## Dry run

Do every local step: checkout, merge, verify, and fix attempts, including local commits so the
result record has SHAs. Do **not** push, comment, label, or create labels. When a PR is done, reset
the local branch with `git reset --hard origin/<branch>` so no dry-run commits remain. Record
outcomes as what *would* have happened.

## Per-PR result record

After each PR, write down this record. The report is built from these records, so keep them
accurate and short:

```
pr: <N>
title: <title>
url: <url>
branch: <branch>
update: <dependency old → new, comma-separated if several>
outcome: already-passing | merged-main-only | fixed | failed
attempts: <0-3>
dry_run: true | false
commits: <pushed (or would-push) short SHAs, or none>
files_changed: <paths, or none>
initial_failure: <one-line error excerpt, or none>
root_cause: <one or two sentences, with source links>
fixes: <semicolon-separated changes, or none>
attempt_log: <attempt 1: …; attempt 2: …; attempt 3: … — or none>
error_excerpt: <last error if failed, else none>
follow_ups: <holds/pins added or things to revisit, or none>
```
