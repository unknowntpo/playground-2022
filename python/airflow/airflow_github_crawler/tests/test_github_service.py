import json
import tempfile
from unittest.mock import Mock, patch

import pytest
import requests

from src.services.github_service import GitHubTrendingService


class TestGitHubTrendingService:
    def setup_method(self):
        self.service = GitHubTrendingService()

    @patch('src.services.github_service.requests.Session.get')
    def test_fetch_trending_repositories_success(self, mock_get):
        mock_html = """
        <html>
            <body>
                <article class="Box-row">
                    <h2 class="h3">
                        <a href="/test/repo1">test/repo1</a>
                    </h2>
                    <p class="col-9">Test repository description</p>
                    <span itemprop="programmingLanguage">Python</span>
                    <a href="/test/repo1/stargazers">1,234</a>
                    <a href="/test/repo1/forks">567</a>
                    <span class="d-inline-block float-sm-right">89 stars today</span>
                </article>
            </body>
        </html>
        """

        mock_response = Mock()
        mock_response.content = mock_html
        mock_response.raise_for_status.return_value = None
        mock_get.return_value = mock_response

        repositories = self.service.fetch_trending_repositories(limit=1)

        assert len(repositories) == 1
        repo = repositories[0]
        assert repo['name'] == 'test/repo1'
        assert repo['url'] == 'https://github.com/test/repo1'
        assert repo['description'] == 'Test repository description'
        assert repo['language'] == 'Python'
        assert repo['stars'] == 1234
        assert repo['forks'] == 567
        assert repo['stars_today'] == 89

    @patch('src.services.github_service.requests.Session.get')
    def test_fetch_trending_repositories_request_error(self, mock_get):
        mock_get.side_effect = requests.RequestException("Network error")

        with pytest.raises(requests.RequestException):
            self.service.fetch_trending_repositories()

    def test_save_to_json(self):
        repositories = [
            {
                'name': 'test/repo1',
                'url': 'https://github.com/test/repo1',
                'description': 'Test repo',
                'language': 'Python',
                'stars': 100,
                'forks': 50,
                'stars_today': 10,
                'crawled_at': '2024-01-01T00:00:00',
                'trending_date': '2024-01-01'
            }
        ]

        with tempfile.NamedTemporaryFile(mode='w', suffix='.json', delete=False) as f:
            filepath = f.name

        self.service.save_to_json(repositories, filepath)

        with open(filepath, 'r') as f:
            data = json.load(f)

        assert data['count'] == 1
        assert len(data['repositories']) == 1
        assert data['repositories'][0]['name'] == 'test/repo1'