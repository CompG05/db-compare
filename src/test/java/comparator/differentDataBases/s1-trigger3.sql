create trigger trigger3
    before update
    on differenttriggers
    for each row
begin
    set new.attr1 = new.attr1 + 3;
end;