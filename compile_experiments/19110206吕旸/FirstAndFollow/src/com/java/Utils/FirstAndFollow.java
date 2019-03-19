package com.java.Utils;

import java.util.Iterator;
import java.util.Stack;

public class FirstAndFollow {

	// first、follow集与src一一对应
	// follow集中字符如果重复了，不是出错，只需要将重复的字符删除即可。
	private String[] src;
	private char[] VT;// 终结字符
	private char[] VN;// 非终结字符
	private StringBuffer[] first;
	private StringBuffer[] follow;

	public FirstAndFollow(String[] src, char[] VT, char[] VN) {
		this.src = src;
		this.VT = VT;
		this.VN = VN;
		first = new StringBuffer[src.length];
		follow = new StringBuffer[src.length];
		for (int i = 0; i < src.length; i++) {
			first[i] = new StringBuffer("");
			follow[i] = new StringBuffer("");
		}
	}

	public void GetFirstFollow() {
		InitFirst();
		IterationFirst(); // 迭代FIRST集
		InitFollow();// 初始化Follow集
		IterationFollow();//迭代follow集
		ImproveFollow();//主要是用于将follow集中多余的字符删除
	}

	private void ImproveFollow() {
		// TODO Auto-generated method stub
		for(int i=0;i<follow.length;i++){
			int j,k;
			for(j=0;j<follow[i].length();j++){
				for(k=j+1;k<follow[i].length();k++){
					if(follow[i].charAt(j)==follow[i].charAt(k)) follow[i].deleteCharAt(k);
				}
			}
		}
	}

	private void IterationFollow() {
		// TODO Auto-generated method stub
		int i;
		for (i = 0; i < src.length; i++) {
			if (src[i].charAt(0) == 'E') {
				break;
			}
		}
		String[] split1 = src[i].split(":");
		String[] split2 = split1[1].split(",");
		// 依次求每个非终结符的follow集
		for (i = 0; i < src.length; i++) {
			char[] visit = new char[VT.length + VN.length];
			char[] appended = new char[VT.length + VN.length];
			int loc = 0;
			int apploc = 0;
			Stack<Character> st = new Stack<Character>();
			for (int j = 0; j < split2.length; j++) {
				for (int k = split2[j].length() - 1; k >= 0; k--) {
					st.push(split2[j].charAt(k));
				}
				st.push('-');
			}
			st.pop();// 弹出多余的分叉符
			while (!st.isEmpty()) {
				char c = st.pop();
				if (c == ',' || c == '-')
					continue;
				if (c == '@') {
					if (!st.isEmpty()) {
						st.pop();
						continue;
					} else {
						break;
					}
				}
				if (IsVT(c)) {
					if (!st.isEmpty()) {
						if (st.lastElement() == ',' || st.lastElement() == '-') {
							st.pop();
						}
						continue;
					} else {
						break;
					}
				} else {
					if (c == src[i].charAt(0)) {
						// 由开始符号推导出该非终结符
						if (!st.isEmpty()) {
							char next = st.lastElement();
							if (next == ',' || next == '-') {
								follow[i].append("#");
								st.pop();
								continue;
							}
							if (!InAppended(appended, next, apploc)) {
								appended[loc] = next;
								apploc++;
								if (IsVT(next)) {
									// 后继字符是终结符，直接加入到follow集中
									follow[i].append(next);
									st.pop();
									if (!st.isEmpty()) {
										if (st.lastElement() == ','
												|| st.lastElement() == '-') {
											st.pop();
										}
										continue;
									} else {
										break;
									}
								} else {
									// 后继字符不是终结符，则将其first集中非空字符加入到follow集中
									AddIntoFollow(c, next);
									st.pop();// 将next字符弹出
									String[] replace = FindReplaceRegu(next);
									// st.push(',');
									for (int j = 0; j < replace.length; j++) {
										for (int k = replace[j].length() - 1; k >= 0; k--) {
											st.push(replace[j].charAt(k)); // 逆序压栈
										}
										st.push('-'); // 将分叉标记符压栈
									}
									st.pop();// 将栈顶的分叉标记符弹出
								}
							} else {
								st.pop();
								if (!st.isEmpty()) {
									if (st.lastElement() == ','
											|| st.lastElement() == '-') {
										st.pop();
										continue;
									}
								} else {
									break;
								}
							}
						} else {
							break;
						}
					} else {
						if (!InVisit(visit, c, loc)) {
							visit[loc] = c;
							loc++;
							String[] replace = FindReplaceRegu(c);
							// st.push(',');
							for (int j = 0; j < replace.length; j++) {
								for (int k = replace[j].length() - 1; k >= 0; k--) {
									st.push(replace[j].charAt(k)); // 逆序压栈
								}
								st.push('-'); // 将分叉标记符压栈
							}
							st.pop();// 将栈顶的分叉标记符弹出
						} else {
							if (!st.isEmpty()) {
								if (st.lastElement() == ','
										|| st.lastElement() == '-') {
									st.pop();
									continue;
								}
							} else {
								break;
							}
						}
					}
				}
			}
		}
	}

	// 判断非终结字符是否能推导出空串
	// private boolean IsDeductionToEmpty(String vn) {
	// String[] split1 = vn.split(":");
	// String[] split2 = vn.split(",");
	// int i;
	// // 存在规则，规则的右部是空串
	// for (String tmp : split2) {
	// if (tmp.equals("@")) {
	// return true;
	// }
	// }
	// ;
	// // 对于所能推导的产生式规则右部的首字符都是终结字符，则说明该非终结字符必然不能推导出空串
	// for (i = 0; i < split2.length; i++) {
	// if (IsVN(split2[i].charAt(0))) {
	// break;
	// }
	// }
	// if (i >= split2.length)
	// return false;
	//
	// // 对于存在规则右部首字符为非终结字符情况的讨论
	// for (i = 0; i < split2.length; i++) {
	// if (IsVN(split2[i].charAt(0))) {
	// Stack<Character> st = new Stack<Character>();
	// for (int j = split2[i].length() - 1; j >= 0; j--) {
	// st.push(split2[i].charAt(j)); // 逆序压栈
	// }
	// String[] replace = FindReplaceRegu(st.pop());
	// if (IsAllFirstEndCharacter(replace)==null) {
	// continue;
	// } else {
	// for(int j=0;j<IsAllFirstEndCharacter(replace).length;j++){
	// //.......
	// //.......
	// //.......
	// }
	// }
	// }
	// }
	// return false;
	// }

	private boolean InAppended(char[] appended, char next, int apploc) {
		// TODO Auto-generated method stub
		for (int i = 0; i < apploc; i++) {
			if (appended[i] == next)
				return true;
		}
		return false;
	}

	private boolean InVisit(char[] visit, char next, int loc) {
		// TODO Auto-generated method stub
		for (int i = 0; i < loc; i++) {
			if (visit[i] == next)
				return true;
		}
		return false;
	}

	/***
	 * 在栈中','表示分界符 在栈中，'-'表示分叉 在栈中，同一路径下，用','表示分界符；在不同路径下，分叉时，用'-'表示分叉。
	 * 例如：1,2-3-4,5,6；如果6出错，则需要将"-4,5,6"全部出栈
	 */
	// 如何判断一个非终结符能否推出空串
	public boolean IsDeductionToEmpty(String vn) {
		String[] split1 = vn.split(":");
		String[] split2 = split1[1].split(",");
		int i;
		// 存在规则，规则的右部是空串
		for (String tmp : split2) {
			if (tmp.equals("@")) {
				return true;
			}
		}
		;
		// 对于所能推导的产生式规则右部的首字符都是终结字符，则说明该非终结字符必然不能推导出空串
		for (i = 0; i < split2.length; i++) {
			if (IsVN(split2[i].charAt(0))) {
				break;
			}
		}
		if (i >= split2.length)
			return false;

		for (i = 0; i < split2.length; i++) {
			if (IsVN(split2[i].charAt(0))) {
				char[] exist = new char[VN.length];
				int loc = 0;
				exist[loc] = split1[0].charAt(0);
				loc++;
				Stack<Character> st = new Stack<Character>();
				for (int j = split2[i].length() - 1; j >= 0; j--) {
					st.push(split2[i].charAt(j)); // 逆序压栈
				}
				// st.push(','); //引入分界符，是为了方便后面出栈。
				// st.pop();//弹出分界符
				char c;
				do {
					c = st.pop();
					// if(InExist((c=st.pop()),exist,loc)){
					// char tmp;
					// while((tmp=st.pop())!=','){
					// }
					// continue;
					// }
					if (c == '@') {
						st.pop();
						continue;
					}

					exist[loc] = c;
					loc++;

					String[] replace = FindReplaceRegu(c);
					if (IsAllFirstEndCharacter(replace) == null) {
						// break;
						// 出栈
						// .....
						// char tmp;
						// while((tmp=st.pop())!='-'){
						// }
						while (!st.empty()) {
							char tmp = st.pop();
							if (tmp == '-')
								break;
						}
					} else if (InExist(replace, exist, loc)) {
						// 判断是否有未出现的非终结字符，依次压栈
						// 若没有，则出栈。
						String[] others = FindOtherReplace(replace, exist, loc);
						if (others == null) {
							while (!st.empty()) {
								char tmp = st.pop();
								if (tmp == '-')
									break;
							}
						} else if (IsAllFirstEndCharacter(others) == null) {
							while (!st.empty()) {
								char tmp = st.pop();
								if (tmp == '-')
									break;
							}
						} else {

							// 将所有可以的规则依次逆序压栈,并将分叉标记符压栈
							st.push(',');
							for (int j = 0; j < others.length; j++) {
								if (IsVN(others[j].charAt(0))) {
									for (int k = others[j].length() - 1; k >= 0; k--) {
										st.push(others[j].charAt(k));
									}
									st.push('-');
								}
							}
							st.pop(); // 弹出多余的分叉标记符
						}
					} else {
						// 对不存在空串规则的讨论和存在空串规则的讨论
						st.push(',');// 确定要替换时，先将分界符入栈
						String[] able = IsAllFirstEndCharacter(replace);
						int len = able.length;
						for (int j = 0; j < able.length; j++) {
							for (int k = able[j].length() - 1; k >= 0; k--) {
								st.push(able[j].charAt(k)); // 逆序压栈
							}
							st.push('-'); // 将分叉标记符压栈
						}
						st.pop();// 将栈顶的分叉标记符弹出
					}
				} while (!st.empty());
			}
		}
		return false;
	}

	private String[] FindOtherReplace(String[] replace, char[] exist, int loc) {
		// TODO Auto-generated method stub
		String[] tmp = null;
		int k = 0;
		int i, j;
		for (i = 0; i < replace.length; i++) {
			for (j = 0; j < loc; j++) {
				if (replace[i].charAt(0) == exist[j])
					break;
			}
			if (j >= loc) {
				tmp[k] = replace[i];
				k++;
			}
		}
		return tmp;
	}

	private boolean InExist(String[] replace, char[] exist, int loc) {
		// TODO Auto-generated method stub
		for (int i = 0; i < replace.length; i++)
			for (int j = 0; j < loc; j++) {
				if (replace[i].charAt(0) == exist[j])
					return true;
			}
		return false;
	}

	private String[] IsAllFirstEndCharacter(String[] replace) {
		// TODO Auto-generated method stub
		String[] able = new String[replace.length];
		for (int i = 0; i < able.length; i++)
			able[i] = null;
		int j = -1;
		for (int i = 0; i < replace.length; i++) {
			if (IsVN(replace[i].charAt(0))) {
				j++;
				able[j] = replace[i];
			} else if (replace[i].equals("@")) {
				j++;
				able[j] = replace[i];
			}
		}
		if (j > -1) {
			return able;
		} else {
			return null;
		}
	}

	private String[] FindReplaceRegu(Character pop) {
		// TODO Auto-generated method stub
		int i;
		for (i = 0; i < src.length; i++) {
			if (src[i].charAt(0) == pop) {
				break;
			}
		}
		if (i < src.length) {
			String[] r1 = src[i].split(":");
			// System.out.println(r1[0]);
			// System.out.println(r1[1]);
			String[] replace = r1[1].split(",");
			return replace;
		}
		return null;
	}

	private void IterationFirst() {
		// TODO Auto-generated method stub
		for (int i = 0; i < src.length; i++) {
			String[] split1 = src[i].split(":");
			String[] split2 = split1[1].split(",");
			Stack<Character> st = new Stack<Character>();

			String allvt = AdvancedInitFirst(split2);
			int len = allvt.length();
			int count = allvt.charAt(len - 1) - '0';
			// for (int j = 0; j < len - 1; j++) {
			// if (!IsExistInFirst(allvt.charAt(j), i)) {
			// first[i].append(allvt.charAt(j));
			// }
			// }
			if (count == split2.length) {
				// 表明所能推导的规则的首字符都是终结字符。first集很好求出，直接结束。
				continue;
			}
			char[] exist = new char[VN.length];
			int loc = 0;
			exist[loc] = split1[0].charAt(0);
			int flag = 0;
			// 对于所有推导规则的首字符非终结字符的情况的讨论
			for (int j = 0; j < split2.length; j++) {
				if (IsVN(split2[j].charAt(0))) {
					for (int k = split2[j].length() - 1; k >= 0; k--) {
						st.push(split2[j].charAt(k)); // 逆序压栈
					}
					st.push('-');
					flag++;
				}
			}
			if (flag > 0) {
				st.pop();
			} else {
				continue;
			}
			char c;
			do {
				c = st.pop();
				// if (c == '@') {
				// st.pop();
				// continue;
				// }

				exist[loc] = c;
				loc++;

				String[] replace = FindReplaceRegu(c);
				if (IsAllFirstEndCharacter(replace) == null) {
					WriteIntoFirst(replace, i);

					while (!st.empty()) {
						char tmp = st.pop();
						if (tmp == '-')
							break;
					}
				} else if (InExist(replace, exist, loc)) {
					// 判断是否有未出现的非终结字符，依次压栈
					// 若没有，则出栈。
					String[] others = FindOtherReplace(replace, exist, loc);
					if (others == null) {
						while (!st.empty()) {
							char tmp = st.pop();
							if (tmp == '-')
								break;
						}
					} else {
						// 将所有可以的规则依次逆序压栈,并将分叉标记符压栈
						st.push(',');
						for (int j = 0; j < others.length; j++) {
							if (IsVN(others[j].charAt(0))) {
								for (int k = others[j].length() - 1; k >= 0; k--) {
									st.push(others[j].charAt(k));
								}
								st.push('-');
							}
							if (IsVT(others[j].charAt(0))) {
								WriteCharIntoFirst(others[j].charAt(0), i);
							}
						}
						st.pop(); // 弹出多余的分叉标记符
					}
				} else {
					st.push(',');// 确定要替换时，先将分界符入栈
					String[] able = IsAllFirstEndCharacter(replace);
					int len1 = able.length;
					for (int j = 0; j < able.length; j++) {
						if (IsVN(able[j].charAt(0))) {
							for (int k = able[j].length() - 1; k >= 0; k--) {
								st.push(able[j].charAt(k)); // 逆序压栈
							}
							st.push('-'); // 将分叉标记符压栈
						}
						if (IsVT(able[j].charAt(0))) {
							WriteCharIntoFirst(able[j].charAt(0), i);
						}
					}
					st.pop();// 将栈顶的分叉标记符弹出
				}

			} while (!st.empty());
		}
	}

	private void WriteCharIntoFirst(char c, int i) {
		// TODO Auto-generated method stub
		if (!IsExistInFirst(c, i)) {
			first[i].append(c);
		}
	}

	private void WriteIntoFirst(String[] src, int who) {
		// TODO Auto-generated method stub
		for (int i = 0; i < src.length; i++) {
			// 判断这个字符是否已经在first集中出现过
			// 不在则写入first集
			if (!IsExistInFirst(src[i].charAt(0), who)) {
				first[who].append(src[i].charAt(0));
			}
		}
	}

	private boolean IsExistInFirst(char c, int who) {
		// TODO Auto-generated method stub
		for (int i = 0; i < first[who].length(); i++) {
			if (first[who].charAt(i) == c)
				return true;
		}
		return false;
	}

	private String AdvancedInitFirst(String[] split2) {
		// TODO Auto-generated method stub
		int count = 0;
		StringBuffer all = new StringBuffer("");
		for (int i = 0; i < split2.length; i++) {
			if (IsVT(split2[i].charAt(0))) {
				all.append(split2[i].charAt(0));
				count++;
			}
		}
		all.append(count);
		return all.toString();
	}

	private void InitFirst() {
		// TODO Auto-generated method stub

		int len = src.length;
		int i, j;
		for (i = 0; i < len; i++) {
			String[] split1 = src[i].split(":");
			String[] split2 = split1[1].split(",");
			if (split1[0].equals("E")) {
				follow[i].append('#');
			}
			for (j = 0; j < split2.length; j++) {
				// 初始化非终结符的first集
				/*****
				 * 当第一个字符为终结字符时，则表示该字符属于其非终结字符的First集 当存在规则的右部位空时，则表示空串输入其First集
				 */
				// if (IsVT(split2[j].charAt(0)) || split2[j].equals("@")) {
				// first[i].append(split2[j].charAt(0));
				// }
				if (IsVT(split2[j].charAt(0))) {
					first[i].append(split2[j].charAt(0));
				}
				// if (IsDeductionToEmpty(src[i])) {
				// first[i].append('@'); // 将空串放入first集
				// }

			}
			if (HasEmptyString(split2)) {
				first[i].append('@');
			}
		}
	}

	private void InitFollow() {
		// TODO Auto-generated method stub
		int i;
		for (i = 0; i < src.length; i++) {
			if (src[i].charAt(0) == 'E') {
				break;
			}
		}
		// 对于产生式规则E->...的进行follow集的初始化
		String[] split1 = src[i].split(":");
		String[] split2 = split1[1].split(",");
		for (i = 0; i < split2.length; i++) {
			for (int j = 0; j < split2[i].length() - 1; j++) {
				AddIntoFollow(split2[i].charAt(j), split2[i].charAt(j + 1));// 将后者的first集加入到前者的follow集中
			}
		}
	}

	private void AddIntoFollow(char source, char target) {
		// TODO Auto-generated method stub
		int i = 0, j = 0;
		for (; i < src.length; i++) {
			if (src[i].charAt(0) == source) {
				break;
			}
		}
		if (IsVT(target)) {
			follow[i].append(target);
		} else {
			for (; j < src.length; j++) {
				if (src[j].charAt(0) == target) {
					break;
				}
			}
			for (int k = 0; k < first[j].length(); k++) {
				if (first[j].charAt(k) != '@') {
					follow[i].append(first[j].charAt(k));
				}
			}
		}
	}

	private boolean HasEmptyString(String[] split2) {
		// TODO Auto-generated method stub
		for (int i = 0; i < split2.length; i++) {
			if (split2[i].equals("@"))
				return true;
		}
		return false;
	}

	private boolean IsVN(char c) {
		// TODO Auto-generated method stub
		for (int i = 0; i < VN.length; i++) {
			if (VN[i] == c)
				return true;
		}
		return false;
	}

	private boolean IsVT(char c) {
		// TODO Auto-generated method stub
		for (int i = 0; i < VT.length; i++) {
			if (VT[i] == c)
				return true;
		}
		return false;
	}

	public void DisplayFirst() {
		// TODO Auto-generated method stub
		for (int i = 0; i < src.length; i++) {
			System.out.println(src[i].charAt(0) + "的first集包含:"
					+ first[i].toString());
		}
	}

	public void DisplayFollow() {
		for (int i = 0; i < src.length; i++) {
			System.out.println(src[i].charAt(0) + "的follow集包含:"
					+ follow[i].toString());
		}
	}
}
