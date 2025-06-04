CREATE TABLE IF NOT EXISTS blog_records (
    record_id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    content TEXT NOT NULL,
    tags JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS blog_subscribers (
    blog_subscribers_id UUID PRIMARY KEY,
    author_id UUID NOT NULL,
    subscriber_id UUID NOT NULL
 );

CREATE TABLE IF NOT EXISTS user_hair_types (
    user_id uuid NOT NULL,
    is_colored boolean,
    porosity VARCHAR(255),
    thickness VARCHAR(255)
);