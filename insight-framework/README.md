# logger

## Log your application

* 


    @Configuration
    @EnableLogger
    public class LoggerConfig {
    
    }
     
* 


    @RestController
    @RequestMapping(path = "/employees")
    public class EmployeeController {
    	
    	@Loggable
    	@GetMapping
    	public List<EmployeeDto> listAllEmployees() {
    	}
    }
    
*

    
    @Loggable(warnOver = 2, warnUnit = TimeUnit.SECONDS)


*


    @Loggable(entered = true)
    @Loggable(skipArgs = true, skipResult = true)
    @Loggable(LogLevel.WARN)
    @Loggable(value = LogLevel.WARN, name = "my-logger-name")
    
## Log messages custom format

    @Configuration
    @EnableLogger
    public class LoggerConfig {
    	@Bean
    	public LoggerFormats loggerFormats() {
    		return LoggerFormats.builder()
    			.before("...")
    			.after("...)
    			...
    			.build();
    	}
    }
