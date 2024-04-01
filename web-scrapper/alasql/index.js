const ferrit = require('ferrit');
const alasql = require('alasql');

(async () => {
  const url = 'https://github.com/trending';
  const browser = await ferrit.open(url);

  // Select the top 10 repository elements
  const repositories = await browser.queryAll('.repo-list-item');

  // Extract repository name and description for each element
  const top10Repos = repositories.slice(0, 10).map(repo => ({
    name: await repo.text('.repo-list-item a'),
    description: await repo.text('.repo-list-item p'),
  }));

  // Convert data to a table-like object for Alasql
  const repoData = {
    repositories: top10Repos
  }

  // Load data into Alasql
  alasql.data('repos', repoData.repositories);

  // Query to get top 10 repositories
  const top10 = alasql('SELECT * FROM repos ORDER BY name LIMIT 10');

  console.log('Top 10 GitHub Repositories:');
  console.table(top10);

  await browser.close();
})();

