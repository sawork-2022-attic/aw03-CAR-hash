package com.example.webpos.web;

import com.example.webpos.biz.PosService;
import com.example.webpos.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PosController {

    private PosService posService;

    @Autowired
    public void setPosService(PosService posService) {
        this.posService = posService;
    }

    @GetMapping("/")
    public String pos(Model model) {
        model.addAttribute("products", posService.products());
        model.addAttribute("cart", posService.getCart());
        return "index";
    }

    @GetMapping("/add")
    public String add(@RequestParam(name = "pid") String pid, Model model) {
        List<Item> items=posService.getCart().getItems();
        boolean exist=false;
        for (Item item:
             items) {
            if(item.getProduct().getId().equals(pid)){
                exist=true;
            }
        }
        if(exist){
            posService.increase(pid,1);
        }else {
            posService.add(pid, 1);
        }
        model.addAttribute("products", posService.products());
        model.addAttribute("cart", posService.getCart());
        return "index";
    }

    @GetMapping("/decrease")
    public String decrease(@RequestParam(name = "pid") String pid, Model model) {
        posService.decrease(pid,1);
        model.addAttribute("products", posService.products());
        model.addAttribute("cart", posService.getCart());
        return "index";
    }

    @GetMapping("/increase")
    public String increase(@RequestParam(name = "pid") String pid, Model model) {
        posService.increase(pid,1);
        model.addAttribute("products", posService.products());
        model.addAttribute("cart", posService.getCart());
        return "index";
    }

    @GetMapping("/del")
    public String del(@RequestParam(name = "pid") String pid, Model model) {
        posService.del(pid);
        model.addAttribute("products", posService.products());
        model.addAttribute("cart", posService.getCart());
        return "index";
    }

    @GetMapping("/subTotal")
    public String subTotal(Model model) {
        model.addAttribute("products", posService.products());
        model.addAttribute("cart", posService.getCart());
        return "index";
    }

    @GetMapping("/cancel")
    public String cancel(Model model) {
        posService.clear();
        model.addAttribute("products", posService.products());
        model.addAttribute("cart", posService.getCart());
        return "index";
    }

}
