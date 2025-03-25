## Giải thích về deterministic trong Temporal Worflow
Explain deterministic behavior in Temporal Workflows and provide an example of a non-deterministic failure using Spring Boot and the Temporal Java SDK.

A workflow's execution must be deterministic so that when it's replayed from the event history, it follows the exact same path. 
That means no non-deterministic operations like random numbers, system time, or external calls during the workflow execution.

Common pitfall:
- Using `new Date()` or `System.currentTimeMillis()` inside the workflow method. 
Since during replay, the time would be different, the workflow would behave differently, leading to a non-deterministic error.

Create a workflow that schedules an activity based on the current time. 
But if the workflow is replayed, the time value during replay might not match the original execution, causing a mismatch in the event history. 
Temporal would detect this and fail the workflow.

Another possibility is using a random number generator inside the workflow. 
Let's see, during the first run, the workflow generates a random number, but during replay, it would generate a different one, leading to a non-determinism error.

In Temporal, when you replay, the code path must be exactly the same. So any branching based on non-deterministic data would cause issues. 

Workflow Deterministic có nghĩa là quá trình thực thi của workflow phải luôn đi theo cùng một logic và tạo ra cùng một kết quả khi được replay từ lịch sử sự kiện (event history). 

Điều này đảm bảo Temporal có thể khôi phục trạng thái workflow sau khi có sự cố (như worker crash) mà không gây ra xung đột hoặc hành vi không mong muốn.

Workflow logic không được phụ thuộc vào các yếu tố không xác định như:
- Thời gian hệ thống (ví dụ: System.currentTimeMillis()).
- Số ngẫu nhiên (ví dụ: Random.nextInt()).
- UUID ngẫu nhiên.
- Truy cập trực tiếp vào database/API bên ngoài.

## Ví dụ 
``` java
public class MyWorkflowImpl implements MyWorkflow {
    private final MyActivities activities = Workflow.newActivityStub(MyActivities.class);

    @Override
    public String execute() {
        // VI PHẠM: Sử dụng thời gian hệ thống trực tiếp trong workflow
        long currentTime = System.currentTimeMillis(); 

        // Giả sử dùng currentTime để quyết định logic
        if (currentTime % 2 == 0) {
            activities.doActionA();
        } else {
            activities.doActionB();
        }

        return "Workflow hoàn thành!";
    }
}
```

Start 1 workflow: W1
### Lần chạy đầu tiên
* Giả sử currentTime = 1715000000000 (chẵn), workflow gọi `doActionA()`.
* Temporal lưu lại lịch sử sự kiện: "Đã gọi doActionA()".

### Khi Replay (ví dụ sau khi worker restart)
* Workflow được replay từ lịch sử. Khi chạy lại, System.currentTimeMillis() trả về giá trị mới (ví dụ: 1715000000001 - lẻ).
* Logic workflow rẽ nhánh gọi `doActionB()`, nhưng lịch sử đã ghi doActionA().
* Lỗi xảy ra: `NonDeterministicWorkflowError`

### Cách Sửa Lỗi Deterministic
Di chuyển các logic không xác định vào Activity:
Activity không cần deterministic vì kết quả của nó được ghi lại trong lịch sử.

``` java
// Sửa lại Workflow Implementation
public class MyWorkflowImpl implements MyWorkflow {
    private final MyActivities activities = Workflow.newActivityStub(MyActivities.class);

    @Override
    public String execute() {
        // Đẩy việc lấy thời gian vào Activity
        long currentTime = activities.getCurrentTime();

        if (currentTime % 2 == 0) {
            activities.doActionA();
        } else {
            activities.doActionB();
        }

        return "Workflow hoàn thành!";
    }
}
```
* getCurrentTime() chạy trong activity, kết quả được lưu vào lịch sử. Khi replay, workflow sử dụng giá trị đã lưu thay vì gọi lại.
``` java
// Thêm method vào Activity
public interface MyActivities {
    @ActivityMethod
    long getCurrentTime(); // Trả về thời gian hệ thống

    @ActivityMethod
    void doActionA();

    @ActivityMethod
    void doActionB();
}
```
* Workflow logic giờ hoàn toàn deterministic vì chỉ phụ thuộc vào dữ liệu từ lịch sử.

## Kết Luận
* Deterministic là yêu cầu bắt buộc để Temporal đảm bảo tính nhất quán của workflow.
* Tránh sử dụng các hàm/giá trị không xác định (thời gian, random, UUID) trực tiếp trong workflow code.

## Giải pháp để tránh bị lỗi non-deterministic
* Đẩy các logic không xác định vào activity hoặc sử dụng API đặc biệt của Temporal (ví dụ: Workflow.currentTimeMillis()).

