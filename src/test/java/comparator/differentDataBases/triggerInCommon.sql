create trigger trigger1
    before insert
    on differenttriggers
    for each row
begin
    set new.attr2 = upper(new.attr2);
end;