create trigger trigger2
    before insert
    on differenttriggers
    for each row
begin
    set new.attr3 = new.attr3 * 0.2;
end;