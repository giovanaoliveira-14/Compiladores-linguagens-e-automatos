; ModuleID = 'module'
declare i32 @printf(i8*, ...)
declare i32 @puts(i8*)

@.fmt_int = private constant [4 x i8] c"%d\0A\00"

define i32 @main() {
entry:
  %a = alloca i32
  %b = alloca i32
  store i32 10, i32* %a
  store i32 20, i32* %b
  %t0 = load i32, i32* %a
  %t1 = load i32, i32* %b
  %t2 = add i32 %t0, %t1
  %t3 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.fmt_int, i32 0, i32 0), i32 %t2)
  %t4 = load i32, i32* %a
  %t5 = load i32, i32* %b
  %t6 = icmp slt i32 %t4, %t5
  br i1 %t6, label %L0, label %L1
L0:
  call i32 @puts(i8* getelementptr inbounds ([8 x i8], [8 x i8]* @.str0, i32 0, i32 0))
  br label %L2
L1:
  call i32 @puts(i8* getelementptr inbounds ([12 x i8], [12 x i8]* @.str1, i32 0, i32 0))
  br label %L2
L2:
  ret i32 0
}
@.str0 = private constant [8 x i8] c"a menor\00"
@.str1 = private constant [12 x i8] c"a nao menor\00"

