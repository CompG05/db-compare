create trigger example_trigger
    before insert
    on example
    for each row
begin
    set new.attr2 = upper(new.attr2);
end;
