public class SequenceAlignment {

    static Character[] x = {' ', 'a', 'c', 'a', 'g', 'a', 'a', 'g', 't', 'a'}; // 짧은 염기서열
    static Character[] y = {' ', 'a', 'c', 't', 'g', 'a', 'g', 't', 't', 'a', 'a'}; // 긴 염기서열 (기준)
    //    static Character[] y = {' ','g','a','a','c','g'};
//    static Character[] x = {' ','g','a','c','t'};
    static int lx = x.length; // 공백을 직접 넣어줘 염기서열 길이+1
    static int ly = y.length; // 공백을 직접 넣어줘 염기서열 길이+1

    static final int q = -2; // mismatch penalty
    static int[][] p = new int[lx][ly]; // p 행렬
    static int[][] a = new int[lx][ly]; // a 행렬
    static int[][] dataFrom = new int[lx][ly]; // 데이터가 어디에서 왔는지 (오른쪽 0, 대각선 1, 위쪽 2 저장)
    static int[][] sameExist = new int[lx][ly]; // 같은 max값의 다른 데이터가 있는지

    public static void sequence_Alignment(int lx, int ly) {

        /* a가 -의 ascii(45)로 채워짐 */
        for (int i = 0; i < lx; i++) {
            for (int j = 0; j < ly; j++) {
                a[i][j] = '-';
            }
        }

        /*0번째 row column 초기화*/
        for (int i = 0; i < lx; i++) {
            a[i][0] = i * q;
        }

        for (int j = 0; j < ly; j++) {
            a[0][j] = j * q;
        }

        /* p행렬 1과 -1로 조건에 따라 넣어주기 */
        for (int i = 1; i < lx; i++) {
            for (int j = 1; j < ly; j++) {
                p[i][j] = x[i].equals(y[j]) ? 1 : -1;
            }
        }

        /* max 값 구해오기 */
        for (int i = 1; i < lx; i++) {
            for (int j = 1; j < ly; j++) {
                int max = a[i][j - 1] + q > a[i - 1][j - 1] + p[i][j] ? a[i][j - 1] + q : a[i - 1][j - 1] + p[i][j];
                max = max > a[i - 1][j] + q ? max : a[i - 1][j] + q;
                a[i][j] = max;
                print(i * j);

                /* 어디서 온 data 인지 확인, 같은 값이 있으면 두개 다 추가 */
                if (max == a[i][j - 1] + q) {
                    dataFrom[i][j] = 0; //왼쪽에서

                    /* 나머지에 max와 같은 값이 있는지 확인 */
                    if (max == a[i - 1][j - 1] + p[i][j]) {
                        sameExist[i][j] = 1;
                    } else if (max == a[i - 1][j] + q) {
                        sameExist[i][j] = 2;
                    }
                } else if (max == a[i - 1][j - 1] + p[i][j]) {
                    dataFrom[i][j] = 1; //대각선에서

                    /* 나머지에 max와 같은 값이 있는지 확인 */
                    if (max == a[i - 1][j] + q) {
                        sameExist[i][j] = 2;
                    }
                } else {
                    dataFrom[i][j] = 2; //오른쪽에서
                }

            }
        }

        /*출력 부분*/
        while (true) {
            /* 기준 염기서열 */
            System.out.print("기준의 염기서열 : \t");
            for (int i = 1; i < ly; i++)
                System.out.print(y[i] + "  ");
            System.out.println();

            /* 비교할 염기서열 */
            int sameCount = 0; // 같은 max 값이 있을 경우 또 출력해주기 위함
            System.out.print("비교된 최적조합 : \t");
            int templx = lx - 1, temply = ly - 1;
            StringBuffer stringBuffer = new StringBuffer();

            while (templx > 0 && temply > 0) {
                if (dataFrom[templx][temply] == 0) {
                    stringBuffer.append("  -");
                    /* 다른 같은 max 값이 존재한다면 */
                    if (sameExist[templx][temply] != 0) {
                        dataFrom[templx][temply] = sameExist[templx][temply];
                        sameCount++;
                        sameExist[templx][temply] = 0;
                    }
                    temply--;
                } else if (dataFrom[templx][temply] == 1) {
                    stringBuffer.append("  " + x[templx]);
                    /* 다른 같은 max 값이 존재한다면 */
                    if (sameExist[templx][temply] != 0) {
                        dataFrom[templx][temply] = sameExist[templx][temply];
                        sameCount++;
                        sameExist[templx][temply] = 0;
                    }
                    templx--;
                    temply--;
                } else if (dataFrom[templx][temply] == 2) {
                    stringBuffer.append("  " + x[templx]);
                    /* 다른 같은 max 값이 존재한다면 */
                    if (sameExist[templx][temply] != 0) {
                        dataFrom[templx][temply] = sameExist[templx][temply];
                        sameCount++;
                        sameExist[templx][temply] = 0;
                    }
                    templx--;
                }
            }
            System.out.println(stringBuffer.reverse());
            System.out.println("점수 : " + a[lx - 1][ly - 1] + "\n");


            //더이상 출력할 경우가 없으면 while문 빠져나가기
            if (sameCount == 0) {
                break;
            }
            sameCount--; // 0이 아니면(아직 출력할게 남아 있으면) 하나를 줄여주고 다음 경우를 출력해줌
        }
    }

    /* 행렬 출력문 */
    public static void print(int k) {
        System.out.println("------------------------------------- " + k + "번째 계산 결과 -------------------------------------");
        System.out.print(" \t\t");
        for (int i = 0; i < ly; i++) {
            System.out.print(y[i] + "\t\t");
        }
        System.out.println();
        for (int i = 0; i < lx; i++) {
            System.out.print(x[i] + "\t\t");
            for (int j = 0; j < ly; j++) {
                if (a[i][j] != 45) {
                    System.out.print(a[i][j] + "\t\t");
                } else {
                    System.out.print((char) a[i][j] + "\t\t");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        sequence_Alignment(lx, ly);
    }
}
