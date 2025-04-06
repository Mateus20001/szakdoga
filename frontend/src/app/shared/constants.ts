export interface MenuItem {
    name: string;
    link: string;
    roles: string[];
}
  
export interface MenuDropdown {
    menuName: string;
    menuDropdownList: MenuItem[];
}
export const MenuDropdownMenuObjects: MenuDropdown[] = [
    {
      menuName: "Általános",
      menuDropdownList: [
        {
          name: "Hírek",
          link: "/news",
          roles: []
        },
        {
          name: "Szakokról",
          link: "/aboutMajors",
          roles: []
        },
        {
          name: "Karokról",
          link: "/aboutFaculties",
          roles: []
        }
      ]
    },
    {
        menuName: "Saját adatok",
        menuDropdownList: [
          {
            name: "Személyes adatok",
            link: "/profile",
            roles: [] 
          },
          {
            name: "Elérhetőségek",
            link: "/contacts",
            roles: [] 
          },
          {
            name: "Képzettségek",
            link: "/idk",
            roles: [] 
          },
          {
            name: "Adatmódosítás",
            link: "/profilechange",
            roles: [] 
          }
        ]
      },
      {
        menuName: "Tanulmányok",
        menuDropdownList: [
          {
            name: "Képzés adatok",
            link: "/dashboard",
            roles: ["STUDENT"] 
          },
          { 
            name: "Kurzus felvétele",
            link: "/courseApplication",
            roles: ["STUDENT"]
          },
          { 
            name: "Órarendtervező",
            link: "/timetable-planner",
            roles: ["STUDENT"]
          },
          { 
            name: "Felvett kurzusok",
            link: "/appliedCourses",
            roles: ["STUDENT"]
          },
          { 
            name: "Vizsgajelentkezés",
            link: "/examadmission",
            roles: ["STUDENT"]
          },
          { 
            name: "Felvett vizsgák",
            link: "/examstaken",
            roles: ["STUDENT"]
          }
        ]
      },
      {
        menuName: "Tanítás",
        menuDropdownList: [
          {
            name: "Tanított Hallgatók adatai",
            link: "/studentStatistic",
            roles: ["TEACHER"] 
          },
          { 
            name: "Hallgatók értékelése",
            link: "/gradingStudents",
            roles: ["TEACHER"]
          },
          { 
            name: "Tanított kurzusok",
            link: "/teachedCourses",
            roles: ["TEACHER"]
          },
          { 
            name: "Vizsga meghirdetése",
            link: "/examadd",
            roles: ["TEACHER"]
          },
          { 
            name: "Meghirdetett vizsgák",
            link: "/examsannounced",
            roles: ["TEACHER"]
          }
        ]
      },
      {
        menuName: "Admin Funkciók",
        menuDropdownList: [
          {
            name: "Felhasználó hozzáadása",
            link: "/addProfile",
            roles: ["ADMIN"]
          },
          {
            name: "Hírek hozzáadása",
            link: "/addNews",
            roles: ["ADMIN"]
          },
          {
            name: "Karok hozzáadása",
            link: "/addFaculties",
            roles: ["ADMIN"]
          },
          { 
            name: "Kurzus meghirdetése",
            link: "/addCourse",
            roles: ["ADMIN"]
          },
          { 
            name: "Kurzusok",
            link: "/courses",
            roles: ["TEACHER"]
          },
        ]
      }
    ];