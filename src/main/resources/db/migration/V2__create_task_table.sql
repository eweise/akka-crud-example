CREATE TABLE TASK (
    ID uuid PRIMARY KEY,
    user_id uuid NOT NULL,
    title text NOT NULL,
    details text NULL,
    due_date timestamp,
    complete bool NOT NULL,
    created_at timestamp NOT NULL,
    modified_at timestamp NOT NULL
);