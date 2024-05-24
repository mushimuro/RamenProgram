public class RamenProgram{

    public static void main(String[] args){
        try{
            // arg가 없기 때문에 args[0] 에서 IndexOutOfBoundsException 발생
            RamenCook ramenCook = new RamenCook(Integer.parseInt(args[0]));
            new Thread(ramenCook, "A").start();
            new Thread(ramenCook, "B").start();
            new Thread(ramenCook, "C").start();
            new Thread(ramenCook, "D").start();
        } catch(Exception e){
            // 예외 발생시 무슨 오류인지 출력
            e.printStackTrace();
        }
    }
}

// 쓰레드에서 실행되기 위해 Runnable 상속
class RamenCook implements Runnable{

    private int ramenCount;
    private String[] burners = {"_", "_", "_", "_"};

    // 라면 갯수를 포함해서 생성자 생성
    public RamenCook(int count){
        ramenCount = count;
    }

    // 쓰레드를 실행시키는 run() 메서드를 수정
    @Override
    public void run(){
        while (ramenCount > 0){

            // 해당 버너 키는 인스턴스에 lock 건다
            synchronized(this){
                ramenCount--;
                System.out.println(
                        Thread.currentThread().getName() + ": " + ramenCount + "개 남음"
                );
            }

            for (int i=0; i<burners.length; i++){
                // 버너가 켜져있으면 스킵
                if(!burners[i].equals("_")) continue;

                // 해당 버너 키는 인스턴스에 lock 건다
                synchronized(this){
                    // if(burners[i].equals("_")){
                    burners[i] = Thread.currentThread().getName();
                    System.out.println(
                            "          "
                                    + Thread.currentThread().getName()
                                    + ": [" + (i+1) + "]번 버너 ON"
                    );
                    showBurners();
                    // }
                }

                try{
                    // 버너를 다 키면 2초간 실행 정지
                    Thread.sleep(2000);
                }catch(Exception e){
                    // 예외 발생시 무슨 오류인지 출력
                    e.printStackTrace();
                }

                // 모든 버너가 꺼지는 인스턴스에 lock 건다
                synchronized(this){
                    burners[i] = "_";
                    System.out.println(
                            "                                 "
                                    + Thread.currentThread().getName()
                                    + ": [" + (i+1) + "]번 버너 OFF"
                    );
                    showBurners();
                }
                break;
            }

            try{
                Thread.sleep(Math.round(1000 * Math.random()));
            }catch(Exception e){
                // 예외 발생시 무슨 오류인지 출력
                e.printStackTrace();
            }
        }
    }


    // 현재 모든 버너의 켜짐/꺼짐 상태를 출력  "A D C _"
    private void showBurners(){
        String stringToPrint = "                                                              ";
        for(int i=0; i<burners.length; i++){
            stringToPrint += (" " + burners[i]);
        }
        System.out.println(stringToPrint);
    }
}