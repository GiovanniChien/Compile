using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections;
using System.IO;
namespace test
{

    class clsLexer
    {
        public Hashtable words;//存放保留字
        public Hashtable symbol;//存放其他系统符号
        //public Hashtable mywords;//存放用户定义标识符，未启用
        public char peek;//每次读取的字符
        public StreamReader str;//文件流
        public int line = 1;// 行号
        public bool tokenstatus;//用于保存被吞字符的状态
        char tokenchar;//功能约等于还原被吞字符

        //将界限符存入hash表
        public void PutSymbol(clsDelimiter t)
        {
            symbol.Add(t.sign, t);
        }

        //将保留字存入hash表
        public  void PutReservedWord(clsWord t)
        {
            words.Add(t.lexeme, t);
        }
       
        public clsLexer(StreamReader str)
        {
            words = new Hashtable();
            symbol = new Hashtable();
            //加入关键词
            PutReservedWord(new clsWord("true", Tag.TRUE,-1));
            PutReservedWord(new clsWord("false", Tag.FALSE,-1));
            for (int i = 0; i < clsReservedWord.myReservedWord.Count; i++)
            {
                PutReservedWord(clsReservedWord.myReservedWord[i]);
            }
            //加入界限符
            for (int i = 0; i < clsDelimiter.myDelimiters.Count; i++)
            {
                PutSymbol(clsDelimiter.myDelimiters[i]);
            }
            this.str = str;
            tokenstatus = false;
            tokenchar=' ';
        }


        //从文件流中读取一个字符
        public bool readOneChar()
        {
            try
            {
                peek = (char)str.Read();
            }
            catch (System.Exception ex)
            {
                throw ex;
                return false;
            }
            return true;
        }

        //扫描一个单元
        public clsToken scan()
        {
            if (tokenstatus==false)//如果前面操作，没有误吞字符，那么读一个
            {
                while (readOneChar())
                {
                    //吞掉空格和换行符，已经制表符，遇到普通字符退出
                    if (peek == ' ' || peek == '\t')
                    {
                        continue;
                    }
                    else if (peek == '\n')
                    {
                        line++;
                    }
                    else
                    {
                        break;
                    }
                }
            }
            else//否则用被吞字符
            {
                peek = tokenchar;
                tokenstatus = false;
            }
          

            // 判断是否为数字数字
            if (isDigit(peek))
            {
                int v = 0;
                do
                {
                    v = v * 10 + peek - '0';
                    readOneChar();
                } while (isDigit(peek));
                if (isIdentifierStart(peek))
                {
                    //如数字后跟着的是字符的话，说明这是一个错误的标识符，而不是数字
                    //不管他，还是先读//读完之后，构造时，告诉程序，，这是个错误的标识符
                    StringBuilder b = new StringBuilder(v+"");
                    do
                    {
                        b.Append(peek);
                        readOneChar();
                    } while (isIdentifierStart(peek) || isDigit(peek));
                    Fortokenchar();
                    String s = b.ToString();
                    clsWord w = new clsWord(s, Tag.error, line);
                    return w;
                }
                else if (peek != '.')
                {
                    Fortokenchar();
                    return new clsNum(v, line);
                }
                float x = v; float d = 10;
                for (; ; )
                {
                    readOneChar();
                    if (!isDigit(peek))
                    {
                        Fortokenchar();
                        break;
                    }
                    x += (peek - '0') / d;
                    d = d * 10;
                }
                if (isIdentifierStart(peek))
                {
                    //如数字后跟着的是字符的话，说明这是一个错误的标识符，而不是数字
                    //不管他，还是先读//读完之后，构造时，告诉程序，，这是个错误的标识符
                    string tmpc = x + "";
                    StringBuilder b = new StringBuilder(tmpc);
                    do
                    {
                        b.Append(peek);
                        readOneChar();
                    } while (isIdentifierStart(peek) || isDigit(peek));
                    Fortokenchar();
                    String s = b.ToString();
                    clsWord w = new clsWord(s, Tag.error, line);
                    return w;
                }
                return new clsRealNum(x,line);
            }


            // 判断保留字或标识符
            if (isIdentifierStart(peek))
            {
                StringBuilder b = new StringBuilder();
                do
                {
                    b.Append(peek);
                    readOneChar();
                } while (isIdentifierStart(peek) || isDigit(peek));
                Fortokenchar();
                String s = b.ToString();
                clsWord w;
                if (words.Contains(s))
                {
                    w = (clsWord)words[s];
                    string x = w.lexeme;
                    int y = w.tag;
                    clsWord newW=new clsWord(x,y,line);
                    return newW;
                }
                w = new clsWord(s, Tag.ID,line);
                //先把用户标识符，也放在words表里面，后面再改
               // words.Add(w.lexeme, w);
                return w;
            }

            string stmp = "" ;
            stmp += peek;
            // 判断界限符
            if (symbol.Contains(stmp))
            {
                    clsToken w = (clsToken)symbol[stmp];
                int x = w.tag;
                if (x==Tag.delimiter)
                {
                    clsDelimiter d=(clsDelimiter)symbol[stmp];
                    clsDelimiter newD = new clsDelimiter(d.sign, x, line);
                    return newD;
                }
            }

            clsToken t = new clsToken(peek,line);
            peek = ' ';
            return t;

        }

        public bool isletter(char c)
        {
            if (c >= 'a' && c <= 'z')
            {
                return true;
            }
            if (c >= 'A' && c <= 'Z')
            {
                return true;
            }
            return false;
        }
        public bool isDigit(char c)
        {
            return (c >= '0' && c <= '9');
        }

        public bool isUnderline(char c)
        {
            return c == '_';
        }

        public bool isIdentifierStart(char c)
        {
            return isUnderline(c) || isletter(c);
        }

        public void Fortokenchar()
        {
            if (!(peek == ' ' || peek == '\t'))
            {
                tokenstatus = true;
                tokenchar = peek;
            }
            if (peek == '\n')
            {
                line++;
            }
        }
       
    }
}
