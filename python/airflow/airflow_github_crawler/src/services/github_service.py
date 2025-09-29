import json
import logging
from datetime import datetime
from typing import Dict, List, Optional

import requests
from bs4 import BeautifulSoup

logger = logging.getLogger(__name__)


class GitHubTrendingService:
    def __init__(self):
        self.base_url = "https://github.com/trending"
        self.session = requests.Session()
        self.session.headers.update({
            "User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36"
        })

    def fetch_trending_repositories(
        self,
        language: Optional[str] = None,
        since: str = "daily",
        limit: int = 10
    ) -> List[Dict]:
        url = self.base_url
        params = {"since": since}

        if language:
            params["l"] = language

        try:
            response = self.session.get(url, params=params, timeout=30)
            response.raise_for_status()

            soup = BeautifulSoup(response.content, 'html.parser')
            repositories = []

            repo_articles = soup.find_all('article', class_='Box-row')[:limit]

            for article in repo_articles:
                repo_data = self._extract_repository_data(article)
                if repo_data:
                    repositories.append(repo_data)

            logger.info(f"Successfully fetched {len(repositories)} trending repositories")
            return repositories

        except requests.RequestException as e:
            logger.error(f"Error fetching trending repositories: {e}")
            raise
        except Exception as e:
            logger.error(f"Unexpected error parsing repositories: {e}")
            raise

    def _extract_repository_data(self, article) -> Optional[Dict]:
        try:
            repo_link = article.find('h2', class_='h3').find('a')
            if not repo_link:
                return None

            repo_name = repo_link.get('href').strip('/')
            repo_url = f"https://github.com{repo_link.get('href')}"

            description_elem = article.find('p', class_='col-9')
            description = description_elem.get_text(strip=True) if description_elem else ""

            language_elem = article.find('span', {'itemprop': 'programmingLanguage'})
            language = language_elem.get_text(strip=True) if language_elem else "Unknown"

            stars_elem = article.find('a', href=lambda x: x and '/stargazers' in x)
            stars = 0
            if stars_elem:
                stars_text = stars_elem.get_text(strip=True).replace(',', '')
                try:
                    stars = int(stars_text)
                except (ValueError, TypeError):
                    stars = 0

            forks_elem = article.find('a', href=lambda x: x and '/forks' in x)
            forks = 0
            if forks_elem:
                forks_text = forks_elem.get_text(strip=True).replace(',', '')
                try:
                    forks = int(forks_text)
                except (ValueError, TypeError):
                    forks = 0

            stars_today_elem = article.find('span', class_='d-inline-block float-sm-right')
            stars_today = 0
            if stars_today_elem:
                stars_today_text = stars_today_elem.get_text(strip=True)
                stars_today_text = stars_today_text.replace('stars today', '').replace(',', '').strip()
                try:
                    stars_today = int(stars_today_text)
                except (ValueError, TypeError):
                    stars_today = 0

            return {
                'name': repo_name,
                'url': repo_url,
                'description': description,
                'language': language,
                'stars': stars,
                'forks': forks,
                'stars_today': stars_today,
                'crawled_at': datetime.utcnow().isoformat(),
                'trending_date': datetime.utcnow().strftime('%Y-%m-%d')
            }

        except Exception as e:
            logger.error(f"Error extracting repository data: {e}")
            return None

    def save_to_json(self, repositories: List[Dict], filepath: str) -> None:
        try:
            with open(filepath, 'w', encoding='utf-8') as f:
                json.dump({
                    'repositories': repositories,
                    'count': len(repositories),
                    'generated_at': datetime.utcnow().isoformat()
                }, f, indent=2, ensure_ascii=False)
            logger.info(f"Saved {len(repositories)} repositories to {filepath}")
        except Exception as e:
            logger.error(f"Error saving repositories to JSON: {e}")
            raise