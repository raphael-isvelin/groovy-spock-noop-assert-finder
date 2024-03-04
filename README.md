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
