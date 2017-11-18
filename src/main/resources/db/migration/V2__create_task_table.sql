create table TASK (
    ID uuid not null,
    user_id uuid not null,
    title text not null,
    details text null,
    due_date timestamp,
    complete bool not null,
    created_at timestamp not null,
    modified_at timestamp not null
);