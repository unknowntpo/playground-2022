from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    sqlite_db_name: str = "database.db"
    model_config = SettingsConfigDict(env_file=".env")


settings = Settings()
