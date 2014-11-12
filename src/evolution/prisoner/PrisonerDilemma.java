package evolution.prisoner;

import evolution.RandomNumberGenerator;
import evolution.prisoner.Strategy.Move;

import java.io.File;

public class PrisonerDilemma {

    public static void main(String[] args) {

        //change this to wherever your .class files are
        //File dir = new File("out/production/evaTeaching/evolution/prisoner/strategies/");
        File dir = new File("bin/evolution/prisoner/strategies/");

        String[] stratNames = dir.list();

        Strategy[] strategies = new Strategy[stratNames.length];

        for (int i = 0; i < strategies.length; i++) {

            String name = stratNames[i].substring(0, stratNames[i].length() - ".class".length());

            name = "evolution.prisoner.strategies." + name;

            try {
                Strategy s = (Strategy) Class.forName(name).newInstance();
                System.err.println(s.getName());
                strategies[i] = s;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

        int[] scores = new int[strategies.length];

        for (int r = 0; r < 1000; r++) {
            for (int i = 0; i < strategies.length; i++) {
                for (int j = 0; j < strategies.length; j++) {

                    if (i == j)
                        continue;

                    Strategy s1 = strategies[i];
                    Strategy s2 = strategies[j];

                    System.err.print(s1.getName() + " vs. " + s2.getName() + ": ");

                    int sc1 = 0;
                    int sc2 = 0;

                    String str1 = "";
                    String str2 = "";

                    int stop = RandomNumberGenerator.getInstance().nextInt(101) + 800;
                    for (int m = 0; m < stop; m++) {

                        Move s1Move = s1.nextMove();
                        Move s2Move = s2.nextMove();

                        str1 += s1Move.getLabel();
                        str2 += s2Move.getLabel();

                        Result r1 = new Result(s1Move, s2Move);
                        Result r2 = new Result(s2Move, s1Move);

                        sc1 += r1.getMyScore();
                        sc2 += r2.getMyScore();

                        s1.reward(r1);
                        s2.reward(r2);

                    }

                    System.err.println(sc1 + ":" + sc2);
                    System.err.println("\t" + str1);
                    System.err.println("\t" + str2);

                    scores[i] += sc1;
                    scores[j] += sc2;

                    s1.reset();
                    s2.reset();

                }
            }

            System.err.flush();
                   /* try {
                        //Thread.sleep(200);
                }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
        }

        for (int j = 0; j < scores.length; j++) {
            int max = Integer.MIN_VALUE;
            int maxIdx = 0;

            for (int i = 0; i < scores.length; i++) {
                if (scores[i] > max) {
                    max = scores[i];
                    maxIdx = i;
                }
            }

            System.out.printf("%50s  %d %s", strategies[maxIdx].getName(), scores[maxIdx], strategies[maxIdx].authorName());
            System.out.println();
            scores[maxIdx] = Integer.MIN_VALUE;
        }
    }

}
