#!/bin/sh
export PATH_TO_SPOCK_NOOP_FINDER="$PWD"
alias noop='java -jar $PATH_TO_SPOCK_NOOP_FINDER/build/libs/noop-finder-0.2.jar'
alias noopconv='node $PATH_TO_SPOCK_NOOP_FINDER/parsers/parse-csv.node.js'
alias noopaggr='node $PATH_TO_SPOCK_NOOP_FINDER/parsers/aggregate-reports.node.js'
function writeteam() {
	# replace by whichever way you may have to detect the repo's owner
	echo "const team = \"UNKNOWN\"; module.exports.team = team;" > team.js
}
function findnoop() {
  a=$1
  no_csv=$2
  if test "$a" = "--sub"; then
    findnoopsub $2
  elif test "$a" = "--here"; then
    findnoophere $2
  else
    echo "\033[31mshould pass --here to check a single repo, or --sub to generate an individual report in each subdirectory"
    return 1
  fi
}
function findnoophere() {
  cur="`basename \"$PWD\"`"
  tmp_folder="/tmp/${cur}_$RANDOM"
  tmp_folder_sub="$tmp_folder/$cur"
  rm -rf "$tmp_folder"
  mkdir -p "$tmp_folder_sub"
  rm -rf .report
  echo "CUR: $cur"
  echo "TF: $tmp_folder"
  echo "TFS: $tmp_folder_sub"
  if test "$1" != "--no-csv-gen"; then
    noop
  else
    echo "(not generating a new CSV)"
  fi
  noopconv
  writeteam
  mv noop-report.csv "$tmp_folder_sub"
  mv report.js "$tmp_folder_sub"
  mv team.js "$tmp_folder_sub"
  cd "$tmp_folder"
  noopaggr
  mkdir .report
  mv ".report-aggregate.js" .report/report-aggregate.js
  cp "$PATH_TO_SPOCK_NOOP_FINDER"/www/* .report
  cd -
  mv "$tmp_folder/.report" .
  echo "report generated for $cur at \033[1mfile://$PWD/.report/index.html\033[0"
}
function findnoopsub() {
	for r in $(ls); do
		echo "(file $r)"
		cd $r
		if test "$?" -ne 0; then
			echo "\033[31mcouldn't cd into '$r' - ignoring\033[0m"
    else
      echo "\033[43mrepo: $r\033[0m"
      if test "$1" != "--no-csv-gen"; then
        noop
      else
        echo "(not generating a new CSV)"
      fi
      noopconv
      writeteam
      cd ..
		fi
	done
	noopaggr
  rm -rf .report
  mkdir .report
  mv .report-aggregate.js .report/report-aggregate.js
  cp -r "$PATH_TO_SPOCK_NOOP_FINDER"/www/* .report
  echo "report generated at \033[1mfile://$PWD/.report/index.html\033[0"
}
