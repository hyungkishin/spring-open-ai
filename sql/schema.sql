CREATE EXTENSION IF NOT EXISTS vector; -- PGvector는 PostgreSQL에서 벡터를 저장하고 검색하는 데 사용된다.
CREATE EXTENSION IF NOT EXISTS hstore; -- Hstore은 PostgreSQL에서 key-value 쌍을 저장하는 데 사용된다.
CREATE EXTENSION IF NOT EXISTS "uuid-ossp"; -- UUID는 고유 식별자를 생성하는 데 사용된다.

-- PGvector는 HNSW 인덱스에 대해 최대 2000개의 차원을 지원한다.

CREATE TABLE IF NOT EXISTS vector_store (
                                            id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
                                            content text,
                                            metadata json,
                                            embedding vector(1536) -- 1536 is the default embedding dimension
);

CREATE INDEX ON vector_store USING HNSW (embedding vector_cosine_ops);
