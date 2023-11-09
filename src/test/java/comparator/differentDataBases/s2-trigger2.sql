create trigger trigger2
    after insert
    on differenttriggers
    for each row
begin
    declare var float;
    set var = new.attr3 * 0.2;
end;