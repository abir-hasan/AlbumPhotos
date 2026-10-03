---
name: renovate-fix-report
description: When and how to write the dated report after a Renovate PR fixing run - only if PRs were changed or failed - using the bundled template. Preloaded by the renovate-pr-fixer agent.
user-invocable: false
---

# Renovate fix report

## When to write it

Write a report **only** if at least one PR ended as:
- `fixed` (fix commits pushed),
- `merged-main-only` (a merge from `main` pushed), or
- `failed` (fix attempts made, PR labelled `needs-manual-fix`).

If there were no Renovate PRs, or every PR was `already-passing`, **don't create any file**. Just
print one summary line, e.g. `3 Renovate PRs checked, all already passing — no report written.`

In a dry run, apply the same rule to what *would* have happened: a would-be fix or merge push
counts as a change.

## Where

```bash
mkdir -p reports/renovate
f="reports/renovate/renovate-fixes-$(date +%F).md"
```

If that file already exists, use `renovate-fixes-YYYY-MM-DD-2.md`, then `-3`, and so on. Never
overwrite an earlier report. Leave the file **uncommitted** for the user to review.

## How

Fill in [template.md](template.md) from the per-PR result records:
- Totals in the header count every processed PR, including `already-passing` ones.
- The summary table has one row per PR. Link the PR number to its URL. Pushed commits are short
  SHAs, or `—`.
- **Details** has one subsection per PR that was `fixed` or `merged-main-only`. `already-passing`
  PRs appear only in the table.
- **Failures** has one subsection per `failed` PR, with the last error excerpt (at most ~15
  lines, in a code block), what each attempt tried, and a concrete suggested next step.
- **Follow-ups** collects every `follow_ups` entry, e.g. `allowedVersions` holds added to
  `renovate.json` and when to remove them.
- Write "None" under Failures or Follow-ups when they're empty. Delete the template's comment
  lines.
- In a dry run, put "(dry run — nothing was pushed, commented or labelled)" in the header.

Finally, give the report path in the summary.
