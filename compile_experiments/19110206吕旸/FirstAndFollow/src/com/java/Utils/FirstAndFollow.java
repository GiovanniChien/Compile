package com.java.Utils;

import java.util.Iterator;
import java.util.Stack;

public class FirstAndFollow {

	// first��follow����srcһһ��Ӧ
	// follow�����ַ�����ظ��ˣ����ǳ���ֻ��Ҫ���ظ����ַ�ɾ�����ɡ�
	private String[] src;
	private char[] VT;// �ս��ַ�
	private char[] VN;// ���ս��ַ�
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
		IterationFirst(); // ����FIRST��
		InitFollow();// ��ʼ��Follow��
		IterationFollow();//����follow��
		ImproveFollow();//��Ҫ�����ڽ�follow���ж�����ַ�ɾ��
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
		// ������ÿ�����ս����follow��
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
			st.pop();// ��������ķֲ��
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
						// �ɿ�ʼ�����Ƶ����÷��ս��
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
									// ����ַ����ս����ֱ�Ӽ��뵽follow����
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
									// ����ַ������ս��������first���зǿ��ַ����뵽follow����
									AddIntoFollow(c, next);
									st.pop();// ��next�ַ�����
									String[] replace = FindReplaceRegu(next);
									// st.push(',');
									for (int j = 0; j < replace.length; j++) {
										for (int k = replace[j].length() - 1; k >= 0; k--) {
											st.push(replace[j].charAt(k)); // ����ѹջ
										}
										st.push('-'); // ���ֲ��Ƿ�ѹջ
									}
									st.pop();// ��ջ���ķֲ��Ƿ�����
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
									st.push(replace[j].charAt(k)); // ����ѹջ
								}
								st.push('-'); // ���ֲ��Ƿ�ѹջ
							}
							st.pop();// ��ջ���ķֲ��Ƿ�����
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

	// �жϷ��ս��ַ��Ƿ����Ƶ����մ�
	// private boolean IsDeductionToEmpty(String vn) {
	// String[] split1 = vn.split(":");
	// String[] split2 = vn.split(",");
	// int i;
	// // ���ڹ��򣬹�����Ҳ��ǿմ�
	// for (String tmp : split2) {
	// if (tmp.equals("@")) {
	// return true;
	// }
	// }
	// ;
	// // ���������Ƶ��Ĳ���ʽ�����Ҳ������ַ������ս��ַ�����˵���÷��ս��ַ���Ȼ�����Ƶ����մ�
	// for (i = 0; i < split2.length; i++) {
	// if (IsVN(split2[i].charAt(0))) {
	// break;
	// }
	// }
	// if (i >= split2.length)
	// return false;
	//
	// // ���ڴ��ڹ����Ҳ����ַ�Ϊ���ս��ַ����������
	// for (i = 0; i < split2.length; i++) {
	// if (IsVN(split2[i].charAt(0))) {
	// Stack<Character> st = new Stack<Character>();
	// for (int j = split2[i].length() - 1; j >= 0; j--) {
	// st.push(split2[i].charAt(j)); // ����ѹջ
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
	 * ��ջ��','��ʾ�ֽ�� ��ջ�У�'-'��ʾ�ֲ� ��ջ�У�ͬһ·���£���','��ʾ�ֽ�����ڲ�ͬ·���£��ֲ�ʱ����'-'��ʾ�ֲ档
	 * ���磺1,2-3-4,5,6�����6��������Ҫ��"-4,5,6"ȫ����ջ
	 */
	// ����ж�һ�����ս���ܷ��Ƴ��մ�
	public boolean IsDeductionToEmpty(String vn) {
		String[] split1 = vn.split(":");
		String[] split2 = split1[1].split(",");
		int i;
		// ���ڹ��򣬹�����Ҳ��ǿմ�
		for (String tmp : split2) {
			if (tmp.equals("@")) {
				return true;
			}
		}
		;
		// ���������Ƶ��Ĳ���ʽ�����Ҳ������ַ������ս��ַ�����˵���÷��ս��ַ���Ȼ�����Ƶ����մ�
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
					st.push(split2[i].charAt(j)); // ����ѹջ
				}
				// st.push(','); //����ֽ������Ϊ�˷�������ջ��
				// st.pop();//�����ֽ��
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
						// ��ջ
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
						// �ж��Ƿ���δ���ֵķ��ս��ַ�������ѹջ
						// ��û�У����ջ��
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

							// �����п��ԵĹ�����������ѹջ,�����ֲ��Ƿ�ѹջ
							st.push(',');
							for (int j = 0; j < others.length; j++) {
								if (IsVN(others[j].charAt(0))) {
									for (int k = others[j].length() - 1; k >= 0; k--) {
										st.push(others[j].charAt(k));
									}
									st.push('-');
								}
							}
							st.pop(); // ��������ķֲ��Ƿ�
						}
					} else {
						// �Բ����ڿմ���������ۺʹ��ڿմ����������
						st.push(',');// ȷ��Ҫ�滻ʱ���Ƚ��ֽ����ջ
						String[] able = IsAllFirstEndCharacter(replace);
						int len = able.length;
						for (int j = 0; j < able.length; j++) {
							for (int k = able[j].length() - 1; k >= 0; k--) {
								st.push(able[j].charAt(k)); // ����ѹջ
							}
							st.push('-'); // ���ֲ��Ƿ�ѹջ
						}
						st.pop();// ��ջ���ķֲ��Ƿ�����
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
				// ���������Ƶ��Ĺ�������ַ������ս��ַ���first���ܺ������ֱ�ӽ�����
				continue;
			}
			char[] exist = new char[VN.length];
			int loc = 0;
			exist[loc] = split1[0].charAt(0);
			int flag = 0;
			// ���������Ƶ���������ַ����ս��ַ������������
			for (int j = 0; j < split2.length; j++) {
				if (IsVN(split2[j].charAt(0))) {
					for (int k = split2[j].length() - 1; k >= 0; k--) {
						st.push(split2[j].charAt(k)); // ����ѹջ
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
					// �ж��Ƿ���δ���ֵķ��ս��ַ�������ѹջ
					// ��û�У����ջ��
					String[] others = FindOtherReplace(replace, exist, loc);
					if (others == null) {
						while (!st.empty()) {
							char tmp = st.pop();
							if (tmp == '-')
								break;
						}
					} else {
						// �����п��ԵĹ�����������ѹջ,�����ֲ��Ƿ�ѹջ
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
						st.pop(); // ��������ķֲ��Ƿ�
					}
				} else {
					st.push(',');// ȷ��Ҫ�滻ʱ���Ƚ��ֽ����ջ
					String[] able = IsAllFirstEndCharacter(replace);
					int len1 = able.length;
					for (int j = 0; j < able.length; j++) {
						if (IsVN(able[j].charAt(0))) {
							for (int k = able[j].length() - 1; k >= 0; k--) {
								st.push(able[j].charAt(k)); // ����ѹջ
							}
							st.push('-'); // ���ֲ��Ƿ�ѹջ
						}
						if (IsVT(able[j].charAt(0))) {
							WriteCharIntoFirst(able[j].charAt(0), i);
						}
					}
					st.pop();// ��ջ���ķֲ��Ƿ�����
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
			// �ж�����ַ��Ƿ��Ѿ���first���г��ֹ�
			// ������д��first��
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
				// ��ʼ�����ս����first��
				/*****
				 * ����һ���ַ�Ϊ�ս��ַ�ʱ�����ʾ���ַ���������ս��ַ���First�� �����ڹ�����Ҳ�λ��ʱ�����ʾ�մ�������First��
				 */
				// if (IsVT(split2[j].charAt(0)) || split2[j].equals("@")) {
				// first[i].append(split2[j].charAt(0));
				// }
				if (IsVT(split2[j].charAt(0))) {
					first[i].append(split2[j].charAt(0));
				}
				// if (IsDeductionToEmpty(src[i])) {
				// first[i].append('@'); // ���մ�����first��
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
		// ���ڲ���ʽ����E->...�Ľ���follow���ĳ�ʼ��
		String[] split1 = src[i].split(":");
		String[] split2 = split1[1].split(",");
		for (i = 0; i < split2.length; i++) {
			for (int j = 0; j < split2[i].length() - 1; j++) {
				AddIntoFollow(split2[i].charAt(j), split2[i].charAt(j + 1));// �����ߵ�first�����뵽ǰ�ߵ�follow����
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
			System.out.println(src[i].charAt(0) + "��first������:"
					+ first[i].toString());
		}
	}

	public void DisplayFollow() {
		for (int i = 0; i < src.length; i++) {
			System.out.println(src[i].charAt(0) + "��follow������:"
					+ follow[i].toString());
		}
	}
}
