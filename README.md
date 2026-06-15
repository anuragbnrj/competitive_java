# Competitive Programming (Java)

Solutions, library code, and interview prep, organized so the reusable parts are
indexed by the IDE and every problem stays a standalone, submittable `Main.java`.

## Layout

```
competitive/
├── library/            # reusable code — the only IDE source root
│   ├── *.java          #   package library         (Pair, Trie, TwoPointer, ...)
│   ├── templates/       #   package library.templates          (canonical Template.java)
│   │   └── archive/     #   package library.templates.archive  (legacy templates)
│   ├── io/             #   package library.io      (fast I/O variants)
│   ├── graphs/         #   package library.graphs
│   ├── math/           #   package library.math
│   ├── segmenttree/    #   package library.segmenttree
│   ├── slidingwindow/  #   package library.slidingwindow
│   └── collectionssyntax/
├── reference/
│   └── atcoder-library/   # vendored AtCoder Library (ACL) port — third-party reference, not ours
├── interview/
│   └── paypal-karat/      # interview-prep scratch (PayPal/Karat practice)
├── scratch/                # ad-hoc Solution.java / Test.java / TestLC.java
└── problems/
    ├── codeforces/<round-slug>/<problem-slug>/Main.java
    ├── atcoder/<contest-slug>/<problem-slug>/Main.java
    ├── algozenith/<problem-slug>/Main.java
    └── hackerrank/<kit-slug>/<problem-slug>/Main.java
```

### Naming convention for `problems/`

`problems/<platform>/<contest-or-kit-slug>/<problem-slug>/Main.java`, all kebab-case,
no spaces, no platform name repeated in the slug. Examples:

- `problems/codeforces/round-378-div2/a-grasshopper-and-the-string/`
- `problems/atcoder/abc360/b-vertical-reading/`
- `problems/atcoder/edu-dp/k-stones/`

AlgoZenith problems aren't grouped into contests, so they sit directly under
`problems/algozenith/<problem-slug>/`.

## Why `problems/` is excluded from the module

Every solution is `public class Main` in the default package — that's required by
Codeforces/AtCoder submission format. If all of `problems/` were on the IDE source
root, every `Main.java` would collide as duplicate top-level classes.

`competitive.iml` makes the **module root itself the source root** (so
`library/graphs/FloydWarshall.java` with `package library.graphs;` resolves
correctly) and excludes `problems/`, `reference/`, `interview/`, and `scratch/`
wholesale. Each problem is compiled and run **independently** by the CP helper
plugin (below), exactly as it would be for submission — never as part of a
whole-repo build. There is intentionally no `pom.xml`/Gradle build; `library/`
has no standalone test harness. If you change a `library/` class, verify it by
compiling/running a problem that uses it (or pasting it into one via the
inliner — see below).

## Tooling: replacing AutoCP

This repo previously carried a `.autocp` config (AutoCP, a VS Code extension).
AutoCP is lightly maintained and — like most non-Java-specific CP extensions —
doesn't solve the main Java pain point: **inlining your library code into a
single submittable file**. Its sample testcases and problem URLs have been
migrated into `problems/**/tests/` and each `Main.java` header (below), and
`.autocp` itself has been deleted. Recommended setup going forward:

- **Browser → Competitive Companion**: install the
  [Competitive Companion](https://github.com/jmerle/competitive-companion)
  extension. One click on a problem page sends the statement + sample tests to
  whichever tool below is listening. This is the shared backbone for both editors.

- **IntelliJ (recommended daily driver) → JHelper**: install the
  [JHelper](https://plugins.jetbrains.com/plugin/10403-jhelper) plugin. It:
  - receives problems from Competitive Companion and creates
    `problems/<platform>/.../Main.java` + sample tests,
  - runs all sample tests in-IDE,
  - **inlines** classes from `library/` into your solution before submission —
    this is the feature AutoCP/CPH don't have, and the main reason to prefer
    JHelper for Java. Point JHelper's "Library" setting at `library/` (package
    root = repo root, as set up in `competitive.iml`).

- **VS Code → CPH (Competitive Programming Helper)**: if you want to edit/run
  problems in VS Code too, replace AutoCP with
  [CPH](https://marketplace.visualstudio.com/items?itemName=DivyanshuAgrawal.competitive-programming-helper).
  It's far more actively maintained, has a clean test-case UI, and works with
  Competitive Companion. It does **not** do library inlining.

  JHelper and CPH each keep their own problem databases in their own formats —
  don't try to keep both in perfect sync. Treat `Main.java` plus the committed
  `tests/` files (below) as the source of truth; the extension caches
  (`.cph/`, JHelper's project files, a regenerated `.autocp`) are local and
  gitignored.

### Sample tests

Sample I/O is stored as plain files per problem, e.g.:

```
problems/codeforces/round-4-div2/a-watermelon/
├── Main.java
└── tests/
    ├── 1.in
    └── 1.out
```

so they're versioned and usable regardless of which extension/editor created
them. Both JHelper and CPH can read/write tests in this form.

### Header convention

Every `problems/**/Main.java` starts with:

```
// <Platform> — <Contest/Group> — <Problem name>
// <date solved, if known>
// Problem Link: <url>
```

## Adding a new problem

1. Open the problem page, fire Competitive Companion.
2. Confirm/adjust the destination path so it lands under
   `problems/<platform>/<contest-slug>/<problem-slug>/Main.java`
   (kebab-case, matching the convention above).
3. Solve, run sample tests, and — for Codeforces/AtCoder — use JHelper's
   "copy with libraries" / inline action before pasting into the judge if you
   pulled in anything from `library/`.
