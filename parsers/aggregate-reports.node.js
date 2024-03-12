const fs = require('fs');
const path = require('path');

function aggregateReports() {
    const reportAggregate = {};
    let excludedMatches = [];

    const directories = fs.readdirSync(process.cwd(), { withFileTypes: true })
                          .filter(dirent => dirent.isDirectory())
                          .map(dirent => dirent.name);

    console.log(`found ${directories.length} directories`);
    directories.forEach(dir => {
        const reportFilePath = path.join(process.cwd(), dir, 'report.js');
        const teamFilePath = path.join(process.cwd(), dir, 'team.js');

        if (fs.existsSync(reportFilePath)) {
            try {
                console.log(`found report.js in dir ${dir}`);
                reportAggregate[dir] = require(reportFilePath).report;
                if (reportAggregate[dir].excludedMatches.length > 0) {
                    console.log('  found ' + reportAggregate[dir].excludedMatches.length + ' excluded matches in ' + dir);
                }
                excludedMatches = excludedMatches.concat(reportAggregate[dir].excludedMatches)
                delete reportAggregate[dir].excludedMatches;
                reportAggregate[dir]['repo']['team'] = require(teamFilePath).team.replaceAll(/#.*/g, "");
            } catch (error) {
                console.error(`error loading report from ${reportFilePath}:`, error);
            }
        } else {
            console.warn("folder '" + dir + "' has no report.js");
        }
    });

    return {'aggregate': reportAggregate, 'excluded': excludedMatches};
}

function main() {
    const report = aggregateReports();
    const reportAggregate = report.aggregate;
    const excludedMatches = report.excluded;

    const outputFilePath = path.join(process.cwd(), '.report-aggregate.js');
    const fileContent = `
const reportAggregate = ${JSON.stringify(reportAggregate, null, 2)};
const excludedMatches = ${JSON.stringify(excludedMatches, null, 2)};
if (typeof module !== 'undefined' && module.exports) {
    module.exports.reportAggregate = reportAggregate;
    module.exports.excludedMatches = excludedMatches;
} else {
    window.reportAggregate = reportAggregate;
    window.excludedMatches = excludedMatches;
}
`
    fs.writeFileSync(outputFilePath, fileContent);

    console.log(`aggregate report generated at ${outputFilePath}`);
}

main();
