package vttp.paf.assessmentpractice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vttp.paf.assessmentpractice.model.Booking;
import vttp.paf.assessmentpractice.model.Search;
import vttp.paf.assessmentpractice.service.ListingsService;

@Controller
@RequestMapping(path = "/listings")
public class ListingsController {

    @Autowired
    ListingsService listSvc;
    
    @GetMapping(path = {"", "/"})
    public String getLandingPage(Model model) {

        model.addAttribute("countries", listSvc.getCountries());
        model.addAttribute("search", new Search());
        return "index";
    }

    @GetMapping(path = "search")
    public String postSearch(@Valid @ModelAttribute Search search, BindingResult binding, Model model,
            HttpSession sess) {
        
        if (search.getMin() > search.getMax()) {

            binding.addError(null);
            model.addAttribute("rangeError", "Min must be less than max");
        }

        if (binding.hasErrors()) {
            model.addAttribute("search", search);
            model.addAttribute("countries", listSvc.getCountries());
            return "index";
        }

        model.addAttribute("listings", listSvc.getListings(search));
        model.addAttribute("country", search.getCountry());
        sess.setAttribute("search", search);
        return "listings";
    }

    @GetMapping(path = "{id}")
    public String getListing(@PathVariable("id") String id, Model model, HttpSession sess) {

        model.addAttribute("listing", listSvc.getListing(id));
        model.addAttribute("searchUrl", listSvc.getSearchUrl((Search) sess.getAttribute("search")));
        model.addAttribute("booking", new Booking());

        return "details";
    }

    @PostMapping(path = "/book/{id}")
    public String postBooking(@PathVariable("id") String id, @ModelAttribute Booking booking, Model model,
            HttpSession sess) {

        String rid = listSvc.booking(id, booking);
        if (rid == null) {

            model.addAttribute("listing", listSvc.getListing(id));
            model.addAttribute("searchUrl", listSvc.getSearchUrl((Search) sess.getAttribute("search")));
            model.addAttribute("booking", booking);
            model.addAttribute("bookingError", "Something went wrong with your booking");
            return "details";
        }

        model.addAttribute("rid", rid);
        return "success";        
    }
}
