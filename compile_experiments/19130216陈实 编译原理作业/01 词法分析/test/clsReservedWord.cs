using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace test
{
    class clsReservedWord
    {
        public static List<clsWord> myReservedWord;

        static clsReservedWord()
        {
            myReservedWord = new List<clsWord>();
            myReservedWord.Add(new clsWord("&&", Tag.AND,-1));
            myReservedWord.Add(new clsWord("||", Tag.OR, -1));
            myReservedWord.Add(new clsWord("==", Tag.EQ, -1));
            myReservedWord.Add(new clsWord("!=", Tag.NE, -1));
            myReservedWord.Add(new clsWord("<=", Tag.LE, -1));
            myReservedWord.Add(new clsWord(">=", Tag.GE, -1));
            myReservedWord.Add(new clsWord("auto", Tag.myauto, -1));
            myReservedWord.Add(new clsWord("break", Tag.mybreak, -1));
            myReservedWord.Add(new clsWord("case", Tag.mycase, -1));
            myReservedWord.Add(new clsWord("char", Tag.mychar, -1));
            myReservedWord.Add(new clsWord("const", Tag.myconst, -1));
            myReservedWord.Add(new clsWord("continue", Tag.mycontinue, -1));
            myReservedWord.Add(new clsWord("default", Tag.mydefault, -1));
            myReservedWord.Add(new clsWord("do", Tag.mydo, -1));
            myReservedWord.Add(new clsWord("double", Tag.mydouble, -1));
            myReservedWord.Add(new clsWord("else", Tag.myelse, -1));
            myReservedWord.Add(new clsWord("enum", Tag.myenum, -1));
            myReservedWord.Add(new clsWord("extern", Tag.myextern, -1));
            myReservedWord.Add(new clsWord("float", Tag.myfloat, -1));
            myReservedWord.Add(new clsWord("for", Tag.myfor, -1));
            myReservedWord.Add(new clsWord("goto", Tag.mygoto, -1));
            myReservedWord.Add(new clsWord("if", Tag.myif, -1));
            myReservedWord.Add(new clsWord("int", Tag.myint, -1));
            myReservedWord.Add(new clsWord("long", Tag.mylong, -1));
            myReservedWord.Add(new clsWord("register", Tag.myregister, -1));
            myReservedWord.Add(new clsWord("return", Tag.myreturn, -1));
            myReservedWord.Add(new clsWord("short", Tag.myshort, -1));
            myReservedWord.Add(new clsWord("signed", Tag.mysigned, -1));
            myReservedWord.Add(new clsWord("sizeof", Tag.mysizeof, -1));
            myReservedWord.Add(new clsWord("static", Tag.mystatic, -1));
            myReservedWord.Add(new clsWord("struct", Tag.mystruct, -1));
            myReservedWord.Add(new clsWord("switch", Tag.myswitch, -1));
            myReservedWord.Add(new clsWord("typedef", Tag.mytypedef, -1));
            myReservedWord.Add(new clsWord("union", Tag.myunion, -1));
            myReservedWord.Add(new clsWord("unsigned", Tag.myunsigned, -1));
            myReservedWord.Add(new clsWord("void", Tag.myvoid, -1));
            myReservedWord.Add(new clsWord("volatile", Tag.myvolatile, -1));
            myReservedWord.Add(new clsWord("while", Tag.mywhile, -1));
        }
    }
}
