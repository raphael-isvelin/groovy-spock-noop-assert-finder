const fs = require('fs');
const path = require('path');

function parseCsv(csv) {
    let cwd, repo, repoUrl, mainBranch;
    const errors = [];
    const matches = {};
    const files = {};
    const methods = {};
    const methodsExclMaybe = {};
    const methodsExclMaybeAndSmell = {};

    let currentFileName = "";
    let currentFilePath = "";
    let currentPathFromRepoRoot = "";

    csv
        .split('\n')
        .forEach(line => {
            let flag;

            if (line.startsWith('!!!')) {
                flag = '!!!';
                line = line.substring(3);
            } else if (line.startsWith('>>>')) {
                flag = '>>>';
                line = line.substring(3);
            } else {
                console.warn(`skipping line: '${line}'`);
                return;
            }

            let parts = line
                .split(',')
                .map(part =>
                    part
                        .replace('<!COMMA!>', ',')
                        .replace('<!NEWLINE!>', '\n')
                );

            if (flag === "!!!") {
                if (parts[0] === "CWD") {
                    cwd = parts[1];
                } else if (parts[0] === "REPO") {
                    repo = parts[1];
                } else if (parts[0] === "MAIN_BRANCH") {
                    mainBranch = parts[1];
                } else if (parts[0] === 'ERROR') {
                    errors.push(parts[1]);
                } else if (parts[0] === 'FILE_OPEN') {
                    currentFileName = parts[1];
                    currentFilePath = parts[2];
                    currentPathFromRepoRoot = currentFilePath.replaceAll(cwd + "/", "");
                    if (matches[currentPathFromRepoRoot] === undefined) {
                        matches[currentPathFromRepoRoot] = [];
                    }
                    if (files[currentPathFromRepoRoot] === undefined) {
                        repoUrl = "https://github.com/transferwise/" + repo;
                        files[currentPathFromRepoRoot] = {
                            fileName: currentFileName,
                            pathFromRepoRoot: currentPathFromRepoRoot,
                            githubUrl: repoUrl + "/blob/" + mainBranch + "/" + currentPathFromRepoRoot,
                            matches: matches[currentPathFromRepoRoot],
                        };
                    }
                } else if (parts[0] === "VISIT_METHOD") {
                    if (methods[currentPathFromRepoRoot + ":" + parts[1]] === undefined) {
                        methods[currentPathFromRepoRoot + ":" + parts[1]] = 0;
                    }
                    if (methodsExclMaybe[currentPathFromRepoRoot + ":" + parts[1]] === undefined) {
                        methodsExclMaybe[currentPathFromRepoRoot + ":" + parts[1]] = 0;
                    }
                    if (methodsExclMaybeAndSmell[currentPathFromRepoRoot + ":" + parts[1]] === undefined) {
                        methodsExclMaybeAndSmell[currentPathFromRepoRoot + ":" + parts[1]] = 0;
                    }
                }
            } else if (flag === ">>>") {
                const match = {
                    severity: parts[0],
                    fileName: parts[1],
                    pathFromRepoRoot: currentPathFromRepoRoot,
                    methodName: parts[2],
                    lineNumber: parts[3],
                    scopeType: parts[4],
                    statementClass: parts[5],
                    text: parts[6],
                };
                ++methods[currentPathFromRepoRoot + ":" + parts[2]];
                matches[currentPathFromRepoRoot].push(match);
                if (match.severity !== "MAYBE") {
                    ++methodsExclMaybe[currentPathFromRepoRoot + ":" + parts[2]];
                }
                if (match.severity !== "MAYBE" && match.severity !== "SMELL") {
                    ++methodsExclMaybeAndSmell[currentPathFromRepoRoot + ":" + parts[2]];
                }
            }
        });

    const fileArray = Object.values(files);
    fileArray.sort((a, b) => b.matches.length - a.matches.length);
    const sortedFiles = fileArray.map(file => ({
        file: {
            name: file.fileName,
            pathFromRepoRoot: file.pathFromRepoRoot,
            githubUrl: file.githubUrl,
        },
        matches: file.matches
    }));

    const methodsWithAtLeastOneFailure = Object.values(methods)
        .filter(value => value > 0).length;
    const methodsWithAtLeastOneFailureExclMaybe = Object.values(methodsExclMaybe)
        .filter(value => value > 0).length;
    const methodsWithAtLeastOneFailureExclMaybeAndSmell = Object.values(methodsExclMaybeAndSmell)
        .filter(value => value > 0).length;

    const matchCount = sortedFiles
        .map(match => match.matches.length)
        .reduce((sum, current) => sum + current, 0);

    const matchCountPerSeverity = {
        'SMELL': 0,
        'ERROR': 0,
        'WARN': 0,
        'MAYBE': 0,
    };
    sortedFiles
        .forEach(file =>
            file.matches
                .forEach(match => ++matchCountPerSeverity[match.severity]));
    matchCountPerSeverity['total'] = Object.values(matchCountPerSeverity).reduce((s, c) => s + c, 0);

    return {
        repo: {
            name: repo,
            url: repoUrl,
            mainBranch: mainBranch,
        },
        files,
        sortedFiles,
        matchCountPerSeverity,
        methods,
        methodsWithAtLeastOneFailure,
        methodsWithAtLeastOneFailureExclMaybe,
        methodsWithAtLeastOneFailureExclMaybeAndSmell,
    }
}

function main() {
    let inputFilePath = process.argv[2];

    if (!inputFilePath) {
        inputFilePath = path.join(process.cwd(), 'noop-report.csv');
        console.info("No input CSV file path provided, using default: " + inputFilePath);
    }

    let csv;
    try {
        csv = fs.readFileSync(inputFilePath, 'utf8');
    } catch (err) {
        console.error("Error reading the input file:", err.message);
        process.exit(1);
    }

    const result = parseCsv(csv);

    const outputFilePath = path.join(process.cwd(), 'report.js');
    fs.writeFileSync(outputFilePath, 'const report = ' + JSON.stringify(result, null, 2) + '; module.exports.report = report;');

    console.log(`Report generated at ${outputFilePath}`);
}

main();