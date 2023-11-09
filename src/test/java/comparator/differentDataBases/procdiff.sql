create procedure totally_different_name(in arg1 int, inout arg2 int)
begin
    set arg2 = arg1 + arg2;
end;
