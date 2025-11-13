-- Add optional relation from book to serie and the order within the serie
alter table book add column serie_id integer;
alter table book add column number_in_serie integer;
alter table book add constraint fk_book_serie foreign key (serie_id) references serie(id);
