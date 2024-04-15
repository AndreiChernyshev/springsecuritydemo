package space.cheran.springsecuritydemo.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import space.cheran.springsecuritydemo.model.Developer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/v1/developers")
public class DeveloperRestControllerV1 {

private final List<Developer> developers = Stream.of(
                new Developer(1L, "Ivan", "Ivanov"),
                new Developer(2L, "Sergey", "Sergeev"),
                new Developer(3L, "Petr",  "Petrov")
).collect(Collectors.toList());

    @GetMapping
       public List<Developer> getAll(){
       return developers;
    }

    @GetMapping("{id}")
    public Developer getById(@PathVariable Long id){
        return developers.stream().filter(developer -> developer.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    @PostMapping
    public Developer create(@RequestBody Developer developer){
        this.developers.add(developer);
        return developer;
    }
    @DeleteMapping("{id}")
    public String deleteById(@PathVariable Long id){
        this.developers.removeIf(developer -> developer.getId().equals(id));
        return "Developer deleted with id: " + id;
    }
    //Example with authentication object
    @GetMapping("/username")
    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
    @GetMapping("/developers-read")
    @PreAuthorize("hasAuthority('developers:read')")
    public String authorityRead(){
        return "This is for developers:read authorities";
    }
    @GetMapping("/developers-write")
    @PreAuthorize("hasAuthority('developers:write')")
    public String authorityWrite(){
        return "This is for developers:write authorities";
    }
}
