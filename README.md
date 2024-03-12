# Smell finder for Spock (Groovy)

```groovy
def "a bad test"() {
    given:
        1 == 2
    when:
        def someArray = service.call()
    then:
        myMock.method(_) >> {
            3 == 4
        }
        if (true) {
            5 == 6
        }
        someArray.each {
            it == 10
        }
}
```

**The test above is useless** (and will pass).

In `then:`/`expect:` blocks, Spock allows us to easily assert stuff by simply writing it: `1 == 2` would throw in these blocks. Same for `verify`, `verifyAll`, and `with` closures.

However, everywhere else, `assert` is needed.

This is well-known, but still an easy mistakes to make, and there's likely hundreds of them in any large Spock codebases.

This repo provides a way to detect them, with a limited number of false positives & false negatives cases (check the tests if you're interested).

The report generated can, for instance, be hosted on GitHub Pages, for your teams to ack upon.

### Rough false positive and false negative rates

#### False positives

The numbers could vary a lot based on the codebase, because some specific patterns can lead to a false positive.

You likely will want to have a manual look at a decent percentage of the matches to get a better idea, but if I have to throw some numbers:

- `ERROR`: about **8% false positives**;
- `WARN`: about **62% false positives**;
- `MAYBE`: very high number of false positives — kept them mostly to keep an eye on them, handle them explicitly in the parser;
- `SMELL`: depends on whether you agree it's a smell!

#### False negatives

By definition, way harder to give a number; have a look at the test cases to find a few examples of false negatives (at least, the ones I was able to identify).

### Other smells detected

#### Bad `thrown` checks

`thrown(Exception)`

This is way too generic. Only accepted if stored in a variable, as it's assumed (hoped) that some additional checks will be performed.

#### Not checking mock invocation count

`myMock.call(123)`

Not checking the number of mock invocations (`1 * myMock.call(123)`) is an easy way to fully skip tests.

A `SMELL` is only detected when it's done in a `then` or `expect` block. (Though feel free to tweak the behaviour.)

#### Naming a method `assert<X>`, but returning a `boolean`

A method called `assert*` that returns a `boolean` (instead of actually performing an `assert`),
is a very easy way to enable future mistakes. Will other devs or future-you know that this will be no-op?

```groovy
then:
    if (true) {
        assertObjectIsValid(obj)
    }
```

#### Eventually: avoiding `assert` in closure

From the [Spock documentation](https://spockframework.org/spock/javadoc/2.2/spock/util/concurrent/PollingConditions.html):

> Warning! Avoiding assert keyword in the clojure is only possible if the conditions object type is known during compilation (no "def" on the left side):
PollingConditions conditions = new PollingConditions(timeout: 10, initialDelay: 1.5, factor: 1.25)

(Before Spock 2.0, the `assert` keyword was always required.)

The conditions for `assert` to be required or not aren't always easy to spot; the matcher assumes that it should always be there for safety, and yields a `SMELL` match if missing.

# How to run it

(Java and `node` are required.)

#### Build the jar

```shell
./gradlew build
```

#### Add to your shell

```shell
source install-in-local-shell.sh
```

#### If you want to check a single repo

```shell
cd $YOUR_REPO
findnoop --here
open .report/index.html
```

#### If you want to check multiple repos

```shell
cd $FOLDER_WITH_MULTIPLE_GIT_REPO_FOLDERS
findnoop --sub
open .report/index.html
```

# Correct team name

If you're generating reports for multiple repositories, like all of your company's Spock-tested repos,
you might want to display which team each repo belongs to, instead of just `UNKNOWN`.

To do this, update the `writeteam` method in the `install-in-local-shell.sh` script. (Depends on your setup, but you might want to simply use a `CODEOWNERS` file if present; a company-wide config repository; a hardcoded lookup table; etc.)

Or instead, you can also add the whole repo as a submodule of another repo, create your own installer script, which will source this one, but override the `writeteam` function. This will also **allow you to publish the report with e.g. GitHub Pages**.

# Excluding matches

If you've manually detected false positives, or want to exclude a match from the file report for any other reason, you can update the `file-paths.exclude` and `match-ids.exclude` files.
